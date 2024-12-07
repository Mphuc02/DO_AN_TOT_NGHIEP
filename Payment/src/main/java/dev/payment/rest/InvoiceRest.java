package dev.payment.rest;

import static dev.common.constant.ApiConstant.PAYMENT.*;
import dev.payment.dto.request.PayInCashRequest;
import dev.common.dto.response.payment.InvoiceResponse;
import dev.payment.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(INVOICE_URL)
@RequiredArgsConstructor
public class InvoiceRest {
    private final InvoiceService invoiceService;

    @GetMapping()
    public ResponseEntity<List<InvoiceResponse>> getAll(){
        return ResponseEntity.ok(invoiceService.findAll());
    }

    @GetMapping(ID)
    public ResponseEntity<InvoiceResponse> findById(@PathVariable UUID id){
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @GetMapping(UN_PAID)
    public ResponseEntity<Page<InvoiceResponse>> getUnPaid(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(invoiceService.getUnPaid(pageable));
    }

    @PutMapping(PAY_IN_CASH)
    public ResponseEntity<Object> payInCash(@PathVariable UUID id, @RequestBody PayInCashRequest request){
        invoiceService.handlePayInCash(id, request);
        return ResponseEntity.noContent().build();
    }
}