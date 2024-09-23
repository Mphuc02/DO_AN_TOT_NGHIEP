package dev.medicine.rest;

import static dev.common.constant.ApiConstant.MEDICINE_URL.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.create.CreateImportInvoiceRequest;
import dev.medicine.dto.request.update.UpdateImportInvoiceRequest;
import dev.medicine.dto.response.ImportInvoiceResponse;
import dev.medicine.service.ImportInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(IMPORT_INVOICE_URL)
@RequiredArgsConstructor
public class ImportInvoiceRest {
    private final ImportInvoiceService importInvoiceService;

    @GetMapping()
    public ResponseEntity<List<ImportInvoiceResponse>> getAll(){
        return ResponseEntity.ok(importInvoiceService.getAll());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody CreateImportInvoiceRequest request,
                                         BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_INVOICE);
        }
        return ResponseEntity.ok(importInvoiceService.create(request));
    }

    @PutMapping(ID)
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateImportInvoiceRequest request,
                                         BindingResult result,
                                         @PathVariable UUID id){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_INVOICE);
        }
        return ResponseEntity.ok(importInvoiceService.update(request, id));
    }
}