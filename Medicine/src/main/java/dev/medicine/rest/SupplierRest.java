package dev.medicine.rest;

import static dev.common.constant.ApiConstant.MEDICINE_URL.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.create.CreateSupplierRequest;
import dev.medicine.dto.request.update.UpdateSupplierRequest;
import dev.common.dto.response.medicine.SupplierResponse;
import dev.medicine.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(SUPPLIER_URL)
@RequiredArgsConstructor
public class SupplierRest {
    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAll(){
        return ResponseEntity.ok(supplierService.getAll());
    }

    @PostMapping()
    public ResponseEntity<SupplierResponse> create(@Valid @RequestBody CreateSupplierRequest request,
                                                   BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_SUPPLIER);
        }
        return ResponseEntity.ok(supplierService.create(request));
    }

    @PutMapping(ID)
    public ResponseEntity<SupplierResponse> update(@Valid @RequestBody UpdateSupplierRequest request,
                                                   BindingResult result,
                                                   @PathVariable UUID id){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_SUPPLIER);
        }
        return ResponseEntity.ok(supplierService.update(request, id));
    }
}