package dev.authentication.rest;

import dev.authentication.dto.request.AuthenticationRequest;
import dev.authentication.dto.request.CreateEmployeeRequest;
import dev.authentication.dto.request.RegisterAccountRequest;
import dev.authentication.service.AccountService;
import dev.common.constant.ApiConstant.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(AUTHENTICATION_URL.URL)
public class AuthenticationRest {
    private final AccountService accountService;

    @PostMapping(AUTHENTICATION_URL.REGISTER)
    public String register(@Valid @RequestBody RegisterAccountRequest request, BindingResult result) {
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), AUTHENTICATION_EXCEPTION.FAIL_VALIDATION_ACCOUNT);
        }
        accountService.register(request);
        return "";
    }

    @PostMapping(AUTHENTICATION_URL.AUTHENTICATE)
    public ResponseEntity<Object> authenticate(@Valid @RequestBody AuthenticationRequest request,
                                               BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), AUTHENTICATION_EXCEPTION.FAIL_VALIDATION_ACCOUNT);
        }
        return ResponseEntity.ok(accountService.authenticateUser(request));
    }

    @PostMapping(AUTHENTICATION_URL.EMPLOYEE_AUTHENTICATION)
    public ResponseEntity<Object> employeeAuthentication(@Valid @RequestBody AuthenticationRequest request, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), AUTHENTICATION_EXCEPTION.FAIL_VALIDATION_ACCOUNT);
        }
        return ResponseEntity.ok(accountService.authenticationForEmployee(request));
    }

    @PostMapping(AUTHENTICATION_URL.EMPLOYEE)
    public void registerEmployee(@Valid @RequestBody CreateEmployeeRequest request, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), EMPLOYEE_EXCEPTION.FAIL_VALIDATION_EMPLOYEE);
        }
        accountService.saveEmployee(request);
    }
}