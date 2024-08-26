package dev.authentication.util;

import dev.authentication.entity.Account;
import dev.authentication.dto.request.RegisterAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@RequiredArgsConstructor
public class AccountUtil {
    private final PasswordEncoder passwordEncoder;

    public Account mapFromRegister(RegisterAccountRequest request){
        return Account.builder()
                .userName(request.getUserName())
                .passWord(passwordEncoder.encode(request.getPassWord()))
                .numberPhone(request.getNumberPhone())
                .email(request.getEmail())
                .createdAt(new Date(new java.util.Date().getTime()))
                .build();
    }
}