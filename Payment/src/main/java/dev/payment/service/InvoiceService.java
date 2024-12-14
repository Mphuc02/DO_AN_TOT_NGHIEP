package dev.payment.service;

import com.google.gson.Gson;
import static dev.common.constant.ExceptionConstant.PAYMENT_EXCEPTION;

import dev.common.client.MedicineClient;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CreateInvoiceCommonRequest;
import dev.common.dto.request.ExportMedicineCommonRequest;
import dev.common.dto.request.ExportMedicineDetailCommonRequest;
import dev.common.dto.response.medicine.PaidMedicineCommonResponse;
import dev.common.dto.response.medicine.PaidMedicineDetailCommonResponse;
import dev.common.dto.response.payment.CalculatedMedicinesCost;
import dev.common.exception.BaseException;
import dev.common.util.AuditingUtil;
import dev.payment.config.VnPayConfig;
import dev.payment.dto.request.PaymentRequest;
import dev.common.dto.response.payment.InvoiceDetailResponse;
import dev.common.dto.response.payment.InvoiceResponse;
import dev.payment.entity.ExaminationCost;
import dev.payment.entity.Invoice;
import dev.payment.entity.InvoiceDetail;
import dev.payment.model.InvoiceStatus;
import dev.payment.repository.ExaminationCostRepository;
import dev.payment.repository.InvoiceDetailRepository;
import dev.payment.repository.InvoiceRepository;
import dev.payment.util.InvoiceDetailMapperUtil;
import dev.payment.util.InvoiceMapperUtil;
import dev.payment.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final VnPayConfig vnPayConfig;
    private final MedicineClient medicineClient;

    @Value(KafkaTopicsConstrant.PAY_MEDICINE_IN_CASH)
    private String payMedicineInCashTopic;

    @Value(KafkaTopicsConstrant.PAY_MEDICINE_ONLINE)
    private String payMedicineOnline;

    @Value(KafkaTopicsConstrant.COMPLETED_PAYMENT_INVOICE)
    private String completedPaidInvoice;

    public List<InvoiceResponse> findAll(){
       return invoiceMapperUtil.mapEntitiesToResponses(invoiceRepository.findAll());
    }

    public Page<InvoiceResponse> getUnPaid(Pageable pageable){
            return invoiceRepository.findUnPaidInvoice(pageable).map(invoiceMapperUtil::mapEntityToResponse);
    }

    public InvoiceResponse findById(UUID id){
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> BaseException.buildNotFound().message(PAYMENT_EXCEPTION.INVOICE_NOT_FOUND).build());
        return invoiceMapperUtil.mapEntityToResponse(invoice);
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
    public void handlePayInCash(UUID invoiceId, PaymentRequest request){
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
        ExportMedicineCommonRequest exportMedicineCommonRequest = ExportMedicineCommonRequest.builder()
                .invoiceId(invoice.getId())
                .details(request.getDetails())
                .build();

        kafkaTemplate.send(payMedicineInCashTopic, exportMedicineCommonRequest);
    }

    @Transactional
    @KafkaListener(topics = KafkaTopicsConstrant.PAID_MEDICINE_INVOICE, groupId = KafkaTopicsConstrant.PAYMENT_GROUP)
    public void handlePaidMedicineInvoice(PaidMedicineCommonResponse medicineInvoice){
        Invoice invoice = invoiceRepository.findById(medicineInvoice.getInvoiceId()).orElseThrow(() -> BaseException.buildNotFound().message(PAYMENT_EXCEPTION.INVOICE_NOT_FOUND).build());

        if(!medicineInvoice.isPaidOnline()){
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
        }else{
            InvoiceResponse invoiceResponse = invoiceMapperUtil.mapEntityToResponse(invoice);
            kafkaTemplate.send(completedPaidInvoice, invoiceResponse);
        }

    }

    @Transactional
    public String payByVnPay(HttpServletRequest request, UUID invoiceId, PaymentRequest paymentRequest) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> BaseException.buildNotFound().message(PAYMENT_EXCEPTION.INVOICE_NOT_FOUND).build());
        if(invoice.getDetails() != null){
            invoiceDetailRepository.deleteAll(invoice.getDetails());
            invoice.setDetails(null);
        }
        if(invoice.getPaidAt() != null){
            throw BaseException.buildBadRequest().message(PAYMENT_EXCEPTION.PAID_INVOICE).build();
        }

        long amount = invoice.getExaminationCost().longValue();
        if(paymentRequest.getDetails() != null && !paymentRequest.getDetails().isEmpty()){
            CalculatedMedicinesCost medicineCost = medicineClient.calculateMedicinesCost(paymentRequest.getDetails());
            amount += medicineCost.getCost().longValue();

            List<InvoiceDetail> invoiceDetails = new ArrayList<>();
            for(PaidMedicineDetailCommonResponse medicineInvoiceDetail: medicineCost.getDetails()){
                InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                        .invoice(invoice)
                        .price(medicineInvoiceDetail.getPrice())
                        .quantity(medicineInvoiceDetail.getQuantity())
                        .medicineId(medicineInvoiceDetail.getMedicineId())
                        .build();
                invoiceDetails.add(invoiceDetail);
            }
            invoice.setDetails(invoiceDetails);
            invoice.setOnlinePaymentStatus(InvoiceStatus.WAITING_PAY_ONLINE.getValue());
            invoiceRepository.save(invoice);
        }

        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(invoiceId);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount * 100));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }

    @Transactional
    public String callBackFromVnPay(HttpServletRequest request){
        UUID invoiceId = UUID.fromString(request.getParameter("vnp_OrderInfo"));
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> BaseException.buildNotFound().message(PAYMENT_EXCEPTION.INVOICE_NOT_FOUND).build());
        String status = request.getParameter("vnp_ResponseCode");
        log.info("received call back from vnpay with order id " + invoiceId + " with status = " + status);
        if(!status.equals("00")){
            invoice.setOnlinePaymentStatus(InvoiceStatus.PAY_ONLINE_FAIL.getValue());

            invoiceDetailRepository.deleteAll(invoice.getDetails());
            invoice.setDetails(null);
            invoiceRepository.save(invoice);
            return buildCallBackPaymentUrl(invoiceId, status);
        }

        List<ExportMedicineDetailCommonRequest> medicineDetailRequest = invoiceDetailMapperUtil.mapEntitiesToMedicineDetailRequest(invoice.getDetails());
        invoice.setPaidAt(LocalDateTime.now());
        invoice.setOnlinePaymentStatus(InvoiceStatus.PAY_ONLINE_SUCCESS.getValue());
        invoiceRepository.save(invoice);

        ExportMedicineCommonRequest exportMedicineCommonRequest = ExportMedicineCommonRequest.builder()
                .invoiceId(invoice.getId())
                .details(medicineDetailRequest)
                .build();

        kafkaTemplate.send(payMedicineOnline, exportMedicineCommonRequest);

        return buildCallBackPaymentUrl(invoiceId, status);
    }

    private String buildCallBackPaymentUrl(UUID invoiceId, String status){
        return String.format("http://localhost:3000/employee/receipt/payment/%s?status=%s", invoiceId, status);
    }
}