package dev.medicine.rest;

import static dev.common.constant.ApiConstant.MEDICINE_URL.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.dto.request.ExportMedicineDetailCommonRequest;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.create.CreateMedicineRequest;
import dev.medicine.dto.request.update.UpdateMedicineRequest;
import dev.common.dto.response.medicine.MedicineResponse;
import dev.medicine.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(MEDICINE_URL)
@RequiredArgsConstructor
public class MedicineRest {
    private final MedicineService medicineService;

    @GetMapping()
    public ResponseEntity<List<MedicineResponse>> getAll(){
        return ResponseEntity.ok(medicineService.getAll());
    }

    @GetMapping(ID)
    public ResponseEntity<MedicineResponse> findById(@PathVariable UUID id){
        return ResponseEntity.ok(medicineService.getByID(id));
    }

    @GetMapping(SEARCH)
    public ResponseEntity<List<MedicineResponse>> search(@RequestParam String q){
        return ResponseEntity.ok(medicineService.search(q));
    }

    @PostMapping(CHECK_MEDICINES_EXIST)
    public ResponseEntity<Set<UUID>> checkMedicinesExist(@RequestBody List<UUID> ids){
        return ResponseEntity.ok(medicineService.checkMedicinesExist(ids));
    }

    @PostMapping()
    public ResponseEntity<MedicineResponse> save(@Valid @RequestBody CreateMedicineRequest request,
                                                 BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_MEDICINE);
        }
        return ResponseEntity.ok(medicineService.save(request));
    }

    @PutMapping(ID)
    public ResponseEntity<MedicineResponse> update(@Valid @RequestBody UpdateMedicineRequest request,
                                                   BindingResult result,
                                                   @PathVariable UUID id){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_MEDICINE);
        }
        return ResponseEntity.ok(medicineService.update(request, id));
    }

    @PostMapping(CALCULATE_MEDICINES_COST)
    public ResponseEntity<Object> calculateMedicinesCost(@RequestBody List<ExportMedicineDetailCommonRequest> medicines){
        return ResponseEntity.ok(medicineService.calculateMedicinesCost(medicines));
    }

    @PostMapping(GET_BY_IDS)
    public ResponseEntity<Object> getByIds(@RequestBody List<UUID> ids){
        return ResponseEntity.ok(medicineService.getByIds(ids));
    }
}