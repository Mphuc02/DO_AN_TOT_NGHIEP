package dev.examinationresult.rest;

import static dev.common.constant.ApiConstant.EXAMINATION_RESULT_URL.*;
import dev.common.constant.AuthorizationConstrant;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.examinationresult.dto.request.SaveMedicineConsultationFormRequest;
import dev.common.dto.response.examination_result.MedicineConsultationFormResponse;
import dev.examinationresult.service.MedicineConsultationFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(CONSULTATION_FORM)
@RequiredArgsConstructor
public class MedicineConsultationRest {
    private final MedicineConsultationFormService service;

    @GetMapping()
    public ResponseEntity<List<MedicineConsultationFormResponse>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize(AuthorizationConstrant.DOCTOR)
    @PostMapping()
    public ResponseEntity<MedicineConsultationFormResponse> create(@Valid @RequestBody SaveMedicineConsultationFormRequest request,
                                                                   BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), EXAMINATION_RESULT_EXCEPTION.FAIL_VALIDATION_CONSULTATION_DETAIL);
        }
        return ResponseEntity.ok(service.create(request));
    }

    @PreAuthorize(AuthorizationConstrant.DOCTOR)
    @PutMapping(ID)
    public ResponseEntity<MedicineConsultationFormResponse> update(@Valid @RequestBody SaveMedicineConsultationFormRequest request,
                                                                   BindingResult result,
                                                                   @PathVariable UUID id){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), EXAMINATION_RESULT_EXCEPTION.FAIL_VALIDATION_CONSULTATION_DETAIL);
        }
        return ResponseEntity.ok(service.update(request, id));
    }
}