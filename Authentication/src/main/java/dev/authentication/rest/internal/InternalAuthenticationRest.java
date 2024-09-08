package dev.authentication.rest.internal;

import dev.authentication.service.AccountService;
import static dev.common.constant.ApiConstant.AUTHENTICATION_URL.INTERNAL.*;

import dev.common.dto.request.CreateNewPatientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(URL)
@RequiredArgsConstructor
public class InternalAuthenticationRest {
    private final AccountService accountService;

    @PostMapping()
    public ResponseEntity<Object> saveAccountFrommGreeting(@RequestBody CreateNewPatientRequest request){
        return ResponseEntity.ok(accountService.saveAccountFromGreeting(request));
    }
}