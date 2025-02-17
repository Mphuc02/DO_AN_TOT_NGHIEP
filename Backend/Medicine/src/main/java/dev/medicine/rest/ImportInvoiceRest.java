package dev.medicine.rest;

import static dev.common.constant.ApiConstant.MEDICINE_URL.*;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.create.CreateImportInvoiceRequest;
import dev.common.dto.response.medicine.ImportInvoiceResponse;
import dev.medicine.service.ImportInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(IMPORT_INVOICE_URL)
@RequiredArgsConstructor
public class ImportInvoiceRest {
    private final ImportInvoiceService importInvoiceService;

    @GetMapping()
    public ResponseEntity<List<ImportInvoiceResponse>> search(){
        return ResponseEntity.ok(importInvoiceService.search());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody CreateImportInvoiceRequest request,
                                         BindingResult result){
        if(result.hasErrors()){
            throw new ObjectIllegalArgumentException(result.getAllErrors(), MEDICINE_EXCEPTION.FAIL_VALIDATION_INVOICE);
        }
        return ResponseEntity.ok(importInvoiceService.create(request));
    }
}