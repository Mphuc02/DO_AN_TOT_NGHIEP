package dev.greeting.rest;

import static dev.common.constant.ApiConstant.GREETING_URL.*;

import dev.greeting.dto.request.CreateExaminationFormRequest;
import dev.greeting.service.ExaminationFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EXAMINATION_FORM_URL)
public class ExaminationFormRest {
    private final ExaminationFormService examinationFormService;

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody CreateExaminationFormRequest request){
        return ResponseEntity.ok(examinationFormService.saveWithoutAppointment(request));
    }
}