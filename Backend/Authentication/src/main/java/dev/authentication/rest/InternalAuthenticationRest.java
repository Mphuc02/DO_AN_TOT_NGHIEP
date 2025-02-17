package dev.authentication.rest;

import static dev.common.constant.ApiConstant.AUTHENTICATION_URL.INTERNAL_URL.*;

import dev.authentication.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping(URL)
@RequiredArgsConstructor
public class InternalAuthenticationRest {
    private final AccountService accountService;

    @GetMapping(CHECK_PHONE_NUMBER_EXIST)
    public ResponseEntity<Boolean> checkPhoneNumberNotExist(@RequestParam(name = "number-phone") String phoneNumber,
                                                            @RequestParam(name = "user-id") UUID userId){
        return ResponseEntity.ok(accountService.checkPhoneNumberNotExist(phoneNumber, userId));
    }
}