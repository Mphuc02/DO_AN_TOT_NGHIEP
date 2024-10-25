package dev.authentication.service;

import dev.authentication.client.EmployeeRoleOpenClient;
import static dev.common.constant.RedisPrefixConstant.*;
import dev.authentication.dto.request.*;
import dev.authentication.entity.InvalidToken;
import dev.authentication.repository.InvalidateTokenRepository;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.constant.RedisKeyConstrant;
import dev.authentication.entity.Account;
import dev.authentication.dto.response.AuthenticationResponse;
import dev.authentication.repository.AccountRepository;
import dev.authentication.util.AccountUtil;
import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.request.CommonRegisterEmployeeRequest;
import dev.common.dto.request.CreateNewPatientRequest;
import dev.common.exception.DuplicateException;
import dev.common.exception.FailAuthenticationException;
import dev.common.exception.NotPermissionException;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.common.model.AuthenticatedUser;
import dev.common.model.Permission;
import dev.common.model.TokenType;
import dev.common.service.RedisService;
import dev.common.util.AuditingUtil;
import dev.common.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static dev.common.constant.RedisKeyConstrant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AccountUtil accountUtil;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRoleOpenClient employeeRoleOpenClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final InvalidateTokenRepository invalidateTokenRepository;
    private final AuditingUtil auditingUtil;

    @Value(KafkaTopicsConstrant.CREATE_EMPLOYEE_TOPIC)
    private String createEmployeeTopic;

    @Value(KafkaTopicsConstrant.CREATED_PATIENT_ACCOUNT_SUCCESS_TOPIC)
    private String createdPatientAccountSuccessTopic;

    @Value(KafkaTopicsConstrant.FAIL_CREATE_PATIENT_FROM_GREETING_TOPIC)
    private String failCreatePatientFromGreetingTopic;

    @Transactional
    public void register(RegisterAccountRequest request){
        List<Account> accountsMatchedConditions = accountRepository.findByUserNameOrEmailOrNumberPhone(request.getUserName(),
                                                                                                request.getEmail(),
                                                                                                request.getNumberPhone());
        Account entity = accountUtil.mapFromRegisterRequest(request);
        if(!accountsMatchedConditions.isEmpty()){
            Map<Object, Object> existedFields = new HashMap<>();
            accountsMatchedConditions.forEach(account -> {
                if(account.getUsername().equals(entity.getUsername()))
                    existedFields.put("userName", "Tài khoản đã tồn tại");
                if(account.getEmail().equals(entity.getEmail()))
                    existedFields.put("email", "Email đã tồn tại");
                if(account.getNumberPhone().equals(entity.getNumberPhone()))
                    existedFields.put("numberPhone", "Số điện thoại đã tồn tại");
            });
            throw new ObjectIllegalArgumentException(existedFields, AUTHENTICATION_EXCEPTION.FAIL_VALIDATION_ACCOUNT);
        }
        accountRepository.save(entity);
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request){
        Account account = authenticate(request);
        List<Permission> permissions = List.of(Permission.USER);
        String accessToken = jwtService.generateAccessToken(account, permissions);
        String refreshToken = jwtService.generateRefreshToken(account, permissions);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse authenticationForEmployee(AuthenticationRequest request){
        Account account = authenticate(request);
        List<Permission> permissions = employeeRoleOpenClient.getAllRolesOfEmployee(account.getId());
        if(ObjectUtils.isEmpty(permissions))
            throw new NotPermissionException(EMPLOYEE_EXCEPTION.NOT_PERMISSION);

        String accessToken = jwtService.generateAccessToken(account, permissions);
        String refreshToken = jwtService.generateRefreshToken(account, permissions);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private Account authenticate(AuthenticationRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassWord()));
            return  (Account) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            throw new FailAuthenticationException(AUTHENTICATION_EXCEPTION.FAIL_AUTHENTICATION);
        }
    }

    @Transactional
    public void saveEmployee(CreateEmployeeRequest request){
        if(accountRepository.existsByEmail(request.getEmail()))
            throw new DuplicateException(String.format("Đã tồn tại tài khoản với email: %s", request.getEmail()));
        if(accountRepository.existsByNumberPhone(request.getNumberPhone()))
            throw new DuplicateException(String.format("Đã tồn tại tài khoản với số điện thoại: %s", request.getNumberPhone()));

        Account entity = accountUtil.mapFromRegisterEmployeeRequest(request);
        entity = accountRepository.save(entity);
        CommonRegisterEmployeeRequest registerRequest = accountUtil.createRegisterEmployeeRequest(request, entity.getId());
        registerRequest.setOwner(auditingUtil.getUserLogged().getId());
        kafkaTemplate.send(createEmployeeTopic, registerRequest);
    }

    public boolean checkPhoneNumberNotExist(String phoneNumber, UUID userId){
        UUID findNumberPhone = (UUID) redisService.getValue(USER_PHONE_PREFIX(phoneNumber), UUID.class);
        if(!ObjectUtils.isEmpty(findNumberPhone)){
            return false;
        }

        if(accountRepository.existsByNumberPhone(phoneNumber))
            return false;

        redisService.setValue(USER_PHONE_PREFIX(phoneNumber), userId);
        return true;
    }

    public String exchangeToken(ExchangeTokenRequest request){
        UUID refreshTokenId = jwtUtil.getTokenId(request.getToken());

        String checkRevokedToken = (String) redisService.getValue(RedisKeyConstrant.INVALID_TOKEN_KEY(refreshTokenId), String.class);
        if(!ObjectUtils.isEmpty(checkRevokedToken))
            throw new NotPermissionException("This token has been revoked");

        AuthenticatedUser user = jwtUtil.getUserFromJwt(request.getToken(), TokenType.REFRESH_TOKEN);
        return jwtService.generateAccessToken(Account.builder().id(user.getId()).build(), user.getPermissions() == null ? null : user.getPermissions().stream().toList());
    }

    @Transactional
    public void logout(LogoutRequest request){
        String refreshToken = request.getRefreshToken();
        String accessToken = request.getAccessToken();

        UUID accessTokenId = jwtUtil.getTokenId(accessToken);
        UUID refreshTokenId = jwtUtil.getTokenId(refreshToken);

        InvalidToken invalidRefreshToken = InvalidToken.builder()
                .id(refreshTokenId)
                .time(new Timestamp(new java.util.Date().getTime()))
                .build();

        invalidateTokenRepository.save(invalidRefreshToken);

        redisService.setValue(INVALID_TOKEN_KEY(refreshTokenId), new java.util.Date());
        redisService.setValue(INVALID_TOKEN_KEY(accessTokenId), new java.util.Date());
        SecurityContextHolder.clearContext();
    }

    @KafkaListener(topics = KafkaTopicsConstrant.CREATE_PATIENT_ACCOUNT_FROM_GREETING_TOPIC,
                    groupId = KafkaTopicsConstrant.AUTHENTICATION_GROUP)
    public void saveAccountFromGreeting(CreateNewPatientRequest request){
        UUID userId = (UUID) redisService.getValue(USER_PHONE_PREFIX(request.getNumberPhone()), UUID.class);
        if(ObjectUtils.isEmpty(userId) ||
            !userId.equals(request.getId())){
            kafkaTemplate.send(failCreatePatientFromGreetingTopic, request.getId());
            return;
        }

        Account account = Account.builder()
                .id(request.getId())
                .createdAt(new Date(new java.util.Date().getTime()))
                .numberPhone(request.getNumberPhone())
                .build();
        accountRepository.save(account);

        kafkaTemplate.send(createdPatientAccountSuccessTopic, request);
        redisService.deleteValue(USER_PHONE_PREFIX(request.getNumberPhone()));
    }
}