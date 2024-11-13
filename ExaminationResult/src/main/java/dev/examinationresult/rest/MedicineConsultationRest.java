package dev.examinationresult.rest;

import static dev.common.constant.ApiConstant.EXAMINATION_RESULT_URL.*;
import dev.common.constant.AuthorizationConstant;
import dev.examinationresult.dto.request.SaveMedicineConsultationFormRequest;
import dev.common.dto.response.examination_result.MedicineConsultationFormResponse;
import dev.examinationresult.service.MedicineConsultationFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    @PreAuthorize(AuthorizationConstant.DOCTOR)
    @PostMapping()
    public ResponseEntity<MedicineConsultationFormResponse> create(@Validated @RequestBody SaveMedicineConsultationFormRequest request,
                                                                   BindingResult result){
        return ResponseEntity.ok(service.create(request));
    }

    @PreAuthorize(AuthorizationConstant.DOCTOR)
    @PutMapping(ID)
    public ResponseEntity<MedicineConsultationFormResponse> update(@Validated @RequestBody SaveMedicineConsultationFormRequest request,
                                                                   BindingResult result,
                                                                   @PathVariable UUID id){
        return ResponseEntity.ok(service.update(request, id));
    }

    @PreAuthorize(AuthorizationConstant.DOCTOR)
    @GetMapping(ID)
    public ResponseEntity<MedicineConsultationFormResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(service.findById(id));
    }
}