package dev.payment.service;

import com.google.gson.Gson;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CreateInvoiceCommonRequest;
import dev.payment.entity.ExaminationCost;
import dev.payment.entity.Invoice;
import dev.payment.repository.ExaminationCostRepository;
import dev.payment.repository.InvoiceRepository;
import dev.payment.util.InvoiceMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final Gson gson;
    private final InvoiceMapperUtil invoiceMapperUtil;
    private final ExaminationCostRepository costRepository;

    @KafkaListener(topics = KafkaTopicsConstrant.CREATED_EXAMINATION_RESULT_SUCCESS, groupId = KafkaTopicsConstrant.PAYMENT_GROUP)
    public void handleCreateInvoice(CreateInvoiceCommonRequest request){
        log.info(String.format("Received createInvoiceRequest from kafka: %s", gson.toJson(request)));
        Invoice invoice = invoiceMapperUtil.mapCreateRequestToEntity(request);
        invoice.setCreatedAt(LocalDateTime.now());
        ExaminationCost latesCost = costRepository.findLatestCostApplied(LocalDate.now());
        invoice.setExaminationCost(latesCost.getCost());

        invoiceRepository.save(invoice);
    }
}