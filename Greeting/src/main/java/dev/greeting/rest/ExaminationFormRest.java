package dev.greeting.rest;

import static dev.common.constant.ApiConstant.GREETING_URL.*;
import dev.common.constant.AuthorizationConstant;
import dev.greeting.dto.request.CreateForWithPatientInforRequest;
import dev.greeting.dto.request.CreateFormForFirstTimePatientRequest;
import dev.greeting.service.ExaminationFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EXAMINATION_FORM_URL)
public class ExaminationFormRest {
    private final ExaminationFormService examinationFormService;

    @PreAuthorize(AuthorizationConstant.GREETING_EMPLOYEE)
    @PostMapping
    public ResponseEntity<Object> save(@Validated @RequestBody CreateForWithPatientInforRequest request){
        return ResponseEntity.ok(examinationFormService.saveWithPatientInformation(request));
    }

    @PreAuthorize(AuthorizationConstant.GREETING_EMPLOYEE)
    @PostMapping(FIRST_TIME)
    public ResponseEntity<Object> save(@Validated @RequestBody CreateFormForFirstTimePatientRequest request){
        return ResponseEntity.ok(examinationFormService.saveFirstTimePatient(request));
    }
}