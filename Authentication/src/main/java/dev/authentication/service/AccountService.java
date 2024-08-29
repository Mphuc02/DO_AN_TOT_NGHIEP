package dev.authentication.service;

import dev.authentication.constant.ValueConstant;
import dev.authentication.entity.Account;
import dev.authentication.dto.request.AuthenticationRequest;
import dev.authentication.dto.request.RegisterAccountRequest;
import dev.authentication.dto.response.AuthenticationResponse;
import dev.authentication.repository.AccountRepository;
import dev.authentication.util.AccountUtil;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.FailAuthenticationException;
import dev.common.exception.ObjectIllegalArgumentException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AccountUtil accountUtil;
    private final AuthenticationManager authenticationManager;

    @Value(ValueConstant.JWT.ACCESS_TOKEN_EXPIRATION)
    public int ACCESS_TOKEN_EXPIRATION;

    @Transactional
    public void register(RegisterAccountRequest request){
        Account entity = accountUtil.mapFromRegister(request);
        List<Account> accountsMatchedConditions = accountRepository.findByUserNameOrEmailOrNumberPhone(entity.getUsername(),
                                                                            entity.getEmail(),
                                                                            entity.getNumberPhone());

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

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        //Todo: Trả về thêm refresh token
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassWord()));
            Account account = (Account) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(account, ACCESS_TOKEN_EXPIRATION);
            return new AuthenticationResponse(accessToken, "");
        } catch (AuthenticationException e) {
            throw new FailAuthenticationException(AUTHENTICATION_EXCEPTION.FAIL_AUTHENTICATION);
        }
    }
}