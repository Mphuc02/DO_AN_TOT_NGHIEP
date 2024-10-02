package dev.payment.rest;

import static dev.common.constant.ApiConstant.PAYMENT.*;
import dev.payment.dto.response.InvoiceResponse;
import dev.payment.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(INVOICE_URL)
@RequiredArgsConstructor
public class InvoiceRest {
    private final InvoiceService invoiceService;

    @GetMapping()
    public ResponseEntity<List<InvoiceResponse>> getAll(){
        return ResponseEntity.ok(invoiceService.findAll());
    }
}