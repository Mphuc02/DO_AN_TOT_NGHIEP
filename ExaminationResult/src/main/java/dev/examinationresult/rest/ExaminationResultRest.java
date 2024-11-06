package dev.examinationresult.rest;

import static dev.common.constant.ApiConstant.EXAMINATION_RESULT_URL.*;

import dev.common.constant.AuthorizationConstant;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.examinationresult.dto.request.UpdateExaminationResultRequest;
import dev.common.dto.response.examination_result.ExaminationResultResponse;
import dev.examinationresult.service.ExaminationResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(RESULT_URL)
@RequiredArgsConstructor
public class ExaminationResultRest {
    private final ExaminationResultService examinationResultService;

    @GetMapping(ID)
    public ResponseEntity<ExaminationResultResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(examinationResultService.getById(id));
    }

    @PreAuthorize(AuthorizationConstant.DOCTOR)
    @GetMapping(FIND_WAITING_EXAMINATION_PATIENTS)
    public ResponseEntity<List<ExaminationResultResponse>> findWaitingExaminationPatients(){
        return ResponseEntity.ok(examinationResultService.findWaitingExaminationPatients());
    }

    @PutMapping(ID)
    public ResponseEntity<ExaminationResultResponse> update(@Valid @RequestBody UpdateExaminationResultRequest request,
                                                            BindingResult result,
                                                            @PathVariable UUID id){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), EXAMINATION_RESULT_EXCEPTION.FAIL_VALIDATION_RESULT);
        }
        return ResponseEntity.ok(examinationResultService.update(request, id));
    }

}