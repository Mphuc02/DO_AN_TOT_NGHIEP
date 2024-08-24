package dev.authentication.rest;

import dev.authentication.model.request.AuthenticationRequest;
import dev.authentication.model.request.RegisterAccountRequest;
import dev.authentication.service.AccountService;
import dev.common.constant.ApiConstant.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(AUTHENTICATION.url)
public class AuthenticationRest {
    private final AccountService accountService;

    @PostMapping(AUTHENTICATION.REGISTER)
    public String register(@Valid @RequestBody RegisterAccountRequest request) {
        //Todo: Kiểm tra request hợp lệ không
        accountService.register(request);
        return "";
    }

    @PostMapping(AUTHENTICATION.AUTHENTICATE)
    public ResponseEntity<Object> authenticate(@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(accountService.authenticate(request));
    }
}