package dev.medicine.rest;

import static dev.common.constant.ApiConstant.MEDICINE_URL.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.create.CreateMedicineRequest;
import dev.medicine.dto.request.create.CreatePatientMedicineInvoiceRequest;
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

    @PostMapping(CHECK_MEDICINES_EXIST)
    public ResponseEntity<Set<UUID>> checkMedicinesExist(@RequestBody List<UUID> ids){
        return ResponseEntity.ok(medicineService.checkMedicinesExist(ids));
    }

    @PostMapping(CREATE_PATIENT_MEDICINE_INVOICE)
    public ResponseEntity<Object> createMedicineInvoiceDetail(@Valid @RequestBody CreatePatientMedicineInvoiceRequest request,
                                                              BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_PATIENT_MEDICINE_INVOICE);
        }

        medicineService.createPatientMedicineInvoice(request);
        return ResponseEntity.ok("ok");
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
}