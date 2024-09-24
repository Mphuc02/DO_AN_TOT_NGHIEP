package dev.examinationresult.rest;

import static dev.common.constant.ApiConstant.EXAMINATION_RESULT_URL.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.examinationresult.dto.request.UpdateExaminationResultRequest;
import dev.examinationresult.dto.response.ExaminationResultResponse;
import dev.examinationresult.service.ExaminationResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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