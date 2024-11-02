package dev.greeting.rest;

import static dev.common.constant.ApiConstant.GREETING_URL.*;
import dev.common.constant.AuthorizationConstant;
import dev.greeting.dto.request.CreateForWithPatientInforRequest;
import dev.greeting.dto.request.CreateFormForFirstTimePatientRequest;
import dev.greeting.dto.request.CreateFormWithAppointmentRequest;
import dev.greeting.service.ExaminationFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EXAMINATION_FORM_URL)
public class ExaminationFormRest {
    private final ExaminationFormService examinationFormService;

    @PreAuthorize(AuthorizationConstant.RECEIPT)
    @PostMapping
    public ResponseEntity<Object> save(@Validated @RequestBody CreateForWithPatientInforRequest request){
        return ResponseEntity.ok(examinationFormService.saveWithPatientInformation(request));
    }

    @PreAuthorize(AuthorizationConstant.RECEIPT_ADMIN)
    @GetMapping(FIND_RECEIVED_PATIENTS_TODAY)
    public ResponseEntity<Object> getReceivedPatientToday(){
        return ResponseEntity.ok(examinationFormService.findReceivedPatientsToday());
    }

    @PreAuthorize(AuthorizationConstant.RECEIPT)
    @PostMapping(FIRST_TIME)
    public ResponseEntity<Object> save(@Validated @RequestBody CreateFormForFirstTimePatientRequest request){
        return ResponseEntity.ok(examinationFormService.saveFirstTimePatient(request));
    }

    @PreAuthorize(AuthorizationConstant.RECEIPT)
    @PostMapping(WITH_APPOINTMENT)
    public ResponseEntity<Object> save(@Validated @RequestBody CreateFormWithAppointmentRequest request){
        return ResponseEntity.ok(examinationFormService.saveWithAppointment(request));
    }
}