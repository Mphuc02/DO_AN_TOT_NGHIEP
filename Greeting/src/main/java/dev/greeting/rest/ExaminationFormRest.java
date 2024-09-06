package dev.greeting.rest;

import static dev.common.constant.ApiConstant.GREETING_URL.*;
import static dev.common.constant.ExceptionConstant.*;

import dev.common.client.AddressClient;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.greeting.dto.request.CreateFormWithoutAppointmentRequest;
import dev.greeting.service.ExaminationFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EXAMINATION_FORM_URL)
public class ExaminationFormRest {
    private final ExaminationFormService examinationFormService;

    @PreAuthorize("hasAuthority('TEST')")
    @PostMapping()
    public ResponseEntity<Object> save(@Valid @RequestBody CreateFormWithoutAppointmentRequest request,
                                       BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), GREETING_EXCEPTION.FAIL_VALIDATION_FORM);
        }
        return ResponseEntity.ok(examinationFormService.saveWithoutAppointment(request));
    }
}