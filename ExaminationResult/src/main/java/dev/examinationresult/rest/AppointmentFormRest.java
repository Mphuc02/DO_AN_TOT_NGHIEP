package dev.examinationresult.rest;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.examinationresult.dto.request.CreateAppointmentFormRequest;
import dev.examinationresult.dto.response.AppointmentFormResponse;
import dev.examinationresult.service.AppointmentFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AppointmentFormRest {
    private final AppointmentFormService appointmentFormService;

    @PostMapping
    public ResponseEntity<AppointmentFormResponse> create(@Valid @RequestBody CreateAppointmentFormRequest request,
                                                          BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), EXAMINATION_RESULT_EXCEPTION.FAIL_VALIDATION_APPOINTMENT_FORM);
        }
        return ResponseEntity.ok(appointmentFormService.create(request));
    }
}