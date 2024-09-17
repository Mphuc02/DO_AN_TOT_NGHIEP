package dev.hospitalinformation.rest;

import static dev.common.constant.ApiConstant.HOSPITAL_INFORMATION.*;
import static dev.common.constant.ExceptionConstant.*;
import dev.common.dto.response.DiseaseCommonResponse;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.hospitalinformation.dto.request.CreateDiseaseRequest;
import dev.hospitalinformation.dto.request.UpdateDiseaseRequest;
import dev.hospitalinformation.service.DiseaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(DISEASE_URL)
@RequiredArgsConstructor
public class DiseaseRest {
    private final DiseaseService diseaseService;

    @GetMapping()
    public ResponseEntity<List<DiseaseCommonResponse>> getAll(){
        return ResponseEntity.ok(diseaseService.getAll());
    }

    @PostMapping(CHECK_DISEASES_EXIST)
    public ResponseEntity<List<UUID>> checkDiseasesExist(@RequestBody List<UUID> diseaseIds){
        return ResponseEntity.ok(diseaseService.checkDiseasesExist(diseaseIds));
    }

    @PostMapping()
    public ResponseEntity<DiseaseCommonResponse> save(@Valid @RequestBody CreateDiseaseRequest request,
                                                      BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(),
                    HOSPITAL_INFORMATION_EXCEPTION.FAIL_VALIDATION_DISEASE);
        }
        return ResponseEntity.ok(diseaseService.save(request));
    }

    @PutMapping(ID)
    public ResponseEntity<DiseaseCommonResponse> update(@Valid @RequestBody UpdateDiseaseRequest request,
                                                        BindingResult result,
                                                        @PathVariable UUID id){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(),
                    HOSPITAL_INFORMATION_EXCEPTION.FAIL_VALIDATION_DISEASE);
        }
        return ResponseEntity.ok(diseaseService.update(request, id));
    }
}