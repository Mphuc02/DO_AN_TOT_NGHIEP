package dev.authentication.util;

import dev.authentication.dto.request.CreateEmployeePermission;
import dev.authentication.dto.request.RegisterEmployeeRequest;
import dev.authentication.entity.Account;
import dev.authentication.dto.request.RegisterAccountRequest;
import dev.common.dto.request.CommonRegisterEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Account mapFromRegisterEmployeeRequest(RegisterEmployeeRequest request){
        return Account.builder()
                .userName(request.getEmail())
                .passWord(passwordEncoder.encode(request.getEmail()))
                .numberPhone(request.getNumberPhone())
                .email(request.getEmail())
                .createdAt(new Date(new java.util.Date().getTime()))
                .build();
    }

    public CommonRegisterEmployeeRequest createRegisterEmployeeRequest(RegisterEmployeeRequest request, UUID id){
        return CommonRegisterEmployeeRequest.builder()
                .id(id)
                .dateOfBirth(request.getDateOfBirth())
                .fullName(request.getFullName())
                .introduce(request.getIntroduce())
                .permissions(request.getPermissions().stream()
                                    .map(CreateEmployeePermission::getPermission)
                                    .collect(Collectors.toList()))
                .build();
    }
}