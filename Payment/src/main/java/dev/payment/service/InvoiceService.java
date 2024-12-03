package dev.payment.service;

import com.google.gson.Gson;
import static dev.common.constant.ExceptionConstant.PAYMENT_EXCEPTION;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CreateInvoiceCommonRequest;
import dev.common.dto.request.PayMedicineInCashCommonRequest;
import dev.common.dto.response.medicine.PaidMedicineCommonResponse;
import dev.common.dto.response.medicine.PaidMedicineDetailCommonResponse;
import dev.common.exception.BaseException;
import dev.common.util.AuditingUtil;
import dev.payment.dto.request.PayInCashRequest;
import dev.common.dto.response.payment.InvoiceDetailResponse;
import dev.common.dto.response.payment.InvoiceResponse;
import dev.payment.entity.ExaminationCost;
import dev.payment.entity.Invoice;
import dev.payment.entity.InvoiceDetail;
import dev.payment.repository.ExaminationCostRepository;
import dev.payment.repository.InvoiceDetailRepository;
import dev.payment.repository.InvoiceRepository;
import dev.payment.util.InvoiceDetailMapperUtil;
import dev.payment.util.InvoiceMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final Gson gson;
    private final InvoiceMapperUtil invoiceMapperUtil;
    private final ExaminationCostRepository costRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final InvoiceDetailMapperUtil invoiceDetailMapperUtil;
    private final AuditingUtil auditingUtil;

    @Value(KafkaTopicsConstrant.PAY_MEDICINE_IN_CASH)
    private String payMedicineInCashTopic;

    @Value(KafkaTopicsConstrant.COMPLETED_PAYMENT_INVOICE)
    private String completedPaidInvoice;

    public List<InvoiceResponse> findAll(){
       return invoiceMapperUtil.mapEntitiesToResponses(invoiceRepository.findAll());
    }

    @KafkaListener(topics = KafkaTopicsConstrant.CREATED_EXAMINATION_RESULT_SUCCESS_TOPIC, groupId = KafkaTopicsConstrant.PAYMENT_GROUP)
    public void handleCreateInvoice(CreateInvoiceCommonRequest request){
        log.info(String.format("Received createInvoiceRequest from kafka: %s", gson.toJson(request)));
        Invoice invoice = invoiceMapperUtil.mapCreateRequestToEntity(request);
        invoice.setCreatedAt(LocalDateTime.now());
        ExaminationCost latesCost = costRepository.findLatestCostApplied(LocalDate.now());
        invoice.setExaminationCost(latesCost.getCost());

        invoiceRepository.save(invoice);
    }

    @Transactional
    public void handlePayInCash(UUID invoiceId, PayInCashRequest request){
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> BaseException.buildNotFound().message(PAYMENT_EXCEPTION.INVOICE_NOT_FOUND).build());
        if(invoice.getPaidAt() != null){
            throw BaseException.buildBadRequest().message(PAYMENT_EXCEPTION.PAID_INVOICE).build();
        }

        invoice.setEmployeeId(auditingUtil.getUserLogged().getId());
        if(request.getDetails() == null || request.getDetails().isEmpty()){
            invoice.setPaidAt(LocalDateTime.now());
            InvoiceResponse invoiceResponse = invoiceMapperUtil.mapEntityToResponse(invoiceRepository.saveAndFlush(invoice));
            kafkaTemplate.send(completedPaidInvoice, invoiceResponse);
            return;
        }

        invoiceRepository.saveAndFlush(invoice);
        PayMedicineInCashCommonRequest payMedicineInCashRequest = PayMedicineInCashCommonRequest.builder()
                .invoiceId(invoice.getId())
                .details(request.getDetails())
                .build();

        kafkaTemplate.send(payMedicineInCashTopic, payMedicineInCashRequest);
    }

    @Transactional
    @KafkaListener(topics = KafkaTopicsConstrant.PAID_MEDICINE_INVOICE, groupId = KafkaTopicsConstrant.PAYMENT_GROUP)
    public void handlePaidMedicineInvoice(PaidMedicineCommonResponse medicineInvoice){
        Invoice invoice = invoiceRepository.findById(medicineInvoice.getInvoiceId()).orElseThrow(() -> BaseException.buildNotFound().message(PAYMENT_EXCEPTION.INVOICE_NOT_FOUND).build());
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        for(PaidMedicineDetailCommonResponse medicineInvoiceDetail: medicineInvoice.getDetails()){
            InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                    .invoice(invoice)
                    .price(medicineInvoiceDetail.getPrice())
                    .quantity(medicineInvoiceDetail.getQuantity())
                    .medicineId(medicineInvoiceDetail.getMedicineId())
                    .build();
            invoiceDetails.add(invoiceDetail);
        }

        invoice.setPaidAt(LocalDateTime.now());
        InvoiceResponse invoiceResponse = invoiceMapperUtil.mapEntityToResponse(invoiceRepository.save(invoice));
        List<InvoiceDetailResponse> detailsResponse = invoiceDetailMapperUtil.mapEntitiesToResponses(invoiceDetailRepository.saveAllAndFlush(invoiceDetails));
        invoiceResponse.setDetails(detailsResponse);

        kafkaTemplate.send(completedPaidInvoice, invoiceResponse);
    }
}