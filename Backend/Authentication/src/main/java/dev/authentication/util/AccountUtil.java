package dev.authentication.util;

import dev.authentication.dto.request.CreateEmployeeRequest;
import dev.common.dto.request.CreateWithPermissionRequest;
import dev.authentication.entity.Account;
import dev.authentication.dto.request.RegisterAccountRequest;
import dev.common.dto.request.RegisterEmployeeCommonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountUtil {
    private final PasswordEncoder passwordEncoder;

    public Account mapFromRegisterRequest(RegisterAccountRequest request){
        return Account.builder()
                .userName(request.getUserName())
                .passWord(passwordEncoder.encode(request.getPassWord()))
                .numberPhone(request.getNumberPhone())
                .email(request.getEmail())
                .createdAt(new Date(new java.util.Date().getTime()))
                .build();
    }

    public Account mapFromRegisterEmployeeRequest(CreateEmployeeRequest request){
        return Account.builder()
                .id(UUID.randomUUID())
                .userName(request.getEmail())
                .passWord(passwordEncoder.encode(request.getEmail()))
                .numberPhone(request.getNumberPhone())
                .email(request.getEmail())
                .createdAt(new Date(new java.util.Date().getTime()))
                .build();
    }

    public RegisterEmployeeCommonRequest createRegisterEmployeeRequest(CreateEmployeeRequest request, UUID id){
        return RegisterEmployeeCommonRequest.builder()
                .id(id)
                .dateOfBirth(request.getDateOfBirth())
                .fullName(request.getFullName())
                .introduce(request.getIntroduce())
                .roles(request.getPermissions().stream()
                                    .map(CreateWithPermissionRequest::getRole)
                                    .toList())
                .build();
    }
}