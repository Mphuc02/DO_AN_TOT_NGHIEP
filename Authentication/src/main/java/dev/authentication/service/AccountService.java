package dev.authentication.service;

import dev.authentication.client.EmployeeRoleOpenClient;
import dev.authentication.constant.ValueConstant;
import dev.authentication.dto.request.CreateEmployeeRequest;
import dev.authentication.entity.Account;
import dev.authentication.dto.request.AuthenticationRequest;
import dev.authentication.dto.request.RegisterAccountRequest;
import dev.authentication.dto.response.AuthenticationResponse;
import dev.authentication.repository.AccountRepository;
import dev.authentication.util.AccountUtil;
import dev.common.constant.ExceptionConstant.*;
import dev.common.constant.KafkaConstrant;
import dev.common.dto.request.CommonRegisterEmployeeRequest;
import dev.common.exception.DuplicateException;
import dev.common.exception.FailAuthenticationException;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.common.model.Permission;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value(ValueConstant.JWT.ACCESS_TOKEN_EXPIRATION)
    public int ACCESS_TOKEN_EXPIRATION;

    @Value(KafkaConstrant.TOPICS.CREATE_EMPLOYEE_TOPIC)
    private String CREATE_EMPLOYEE_TOPIC;

    @Transactional
    public void register(RegisterAccountRequest request){
        List<Account> accountsMatchedConditions = accountRepository.findByUserNameOrEmailOrNumberPhone(request.getUserName(),
                                                                                                request.getEmail(),
                                                                                                request.getNumberPhone());
        Account entity = accountUtil.mapFromRegisterRequest(request);
        if(!accountsMatchedConditions.isEmpty()){
            Map<String, String> existedFields = new HashMap<>();
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
        //Todo: Trả về thêm refresh token
        Account account = authenticate(request);
        String accessToken = jwtService.generateToken(account, ACCESS_TOKEN_EXPIRATION, null);
        return new AuthenticationResponse(accessToken, "");
    }

    public AuthenticationResponse authenticationForEmployee(AuthenticationRequest request){
        //Todo: Trả về thêm refresh token
        Account account = authenticate(request);
        List<Permission> permissions = employeeRoleOpenClient.getAllRolesOfEmployee(account.getId());
        String accessToken = jwtService.generateToken(account, ACCESS_TOKEN_EXPIRATION, permissions);
        return new AuthenticationResponse(accessToken, "");
    }

    private Account authenticate(AuthenticationRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassWord()));
            return  (Account) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            throw new FailAuthenticationException(AUTHENTICATION_EXCEPTION.FAIL_AUTHENTICATION);
        }
    }

    public void saveEmployee(CreateEmployeeRequest request){
        if(accountRepository.existsByEmail(request.getEmail()))
            throw new DuplicateException(String.format("Đã tồn tại tài khoản với email: %s", request.getEmail()));

        Account entity = accountUtil.mapFromRegisterEmployeeRequest(request);
        entity = accountRepository.save(entity);
        CommonRegisterEmployeeRequest registerRequest = accountUtil.createRegisterEmployeeRequest(request, entity.getId());
        kafkaTemplate.send(CREATE_EMPLOYEE_TOPIC, registerRequest);
    }
}