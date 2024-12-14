package dev.payment.rest;

import static dev.common.constant.ApiConstant.PAYMENT.*;
import dev.payment.dto.request.PaymentRequest;
import dev.common.dto.response.payment.InvoiceResponse;
import dev.payment.service.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<Object> payInCash(@PathVariable UUID id, @Validated @RequestBody PaymentRequest request){
        invoiceService.handlePayInCash(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(PAY_BY_VNPAY)
    public ResponseEntity<Object> payByVnpay(HttpServletRequest request, @PathVariable UUID id, @RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(invoiceService.payByVnPay(request, id, paymentRequest));
    }

    @GetMapping(VNPAY_CALL_BACK)
    public void vnpayCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String invoiceUrl = invoiceService.callBackFromVnPay(request);
        response.sendRedirect(invoiceUrl);
    }
}