package dev.medicine.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.PayMedicineDetailCommonRequest;
import dev.common.dto.request.PayMedicineInCashCommonRequest;
import dev.common.dto.response.medicine.PaidMedicineCommonResponse;
import dev.common.dto.response.medicine.PaidMedicineDetailCommonResponse;
import dev.common.exception.NotFoundException;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.create.CreateMedicineRequest;
import dev.medicine.dto.request.create.CreatePatientMedicineInvoiceRequest;
import dev.medicine.dto.request.update.UpdateMedicineRequest;
import dev.common.dto.response.medicine.MedicineResponse;
import dev.medicine.entity.ExportInvoiceDetail;
import dev.medicine.entity.Medicine;
import dev.medicine.repository.ExportInvoiceDetailRepository;
import dev.medicine.repository.MedicineRepository;
import dev.medicine.util.MedicineMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineMapperUtil medicineMapperUtil;
    private final ExportInvoiceDetailRepository exportInvoiceDetailRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value(KafkaTopicsConstrant.PAID_MEDICINE_INVOICE)
    private String paidMedicineInvoiceTopic;

    public MedicineResponse getByID(UUID id){
        Medicine medicine = medicineRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException(MEDICINE_EXCEPTION.MEDICINE_NOT_FOUND));
        return medicineMapperUtil.mapEntityToResponse(medicine);
    }

    public List<MedicineResponse> getAll(){
        return medicineMapperUtil.mapEntitiesFromResponses(medicineRepository.findAll());
    }

    public List<MedicineResponse> search(String search){
        if(StringUtils.hasText(search)){
            search = "%" + search.trim().toLowerCase() + "%";
        }
        else{
            search = null;
        }
        return medicineMapperUtil.mapEntitiesFromResponses(medicineRepository.searchByName(search));
    }

    public Set<UUID> checkMedicinesExist(List<UUID> ids){
        return medicineRepository.findAllById(ids)
                            .stream()
                            .map(Medicine::getId)
                            .collect(Collectors.toSet());
    }

    @Transactional
    public void createPatientMedicineInvoice(CreatePatientMedicineInvoiceRequest request){
        List<Medicine> medicines = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(0);
        Map<Object, Object> errorField = new HashMap<>();
        List<ExportInvoiceDetail> details = request.getDetails().stream()
                                .map(detail -> {
                                    Optional<Medicine> medicineOptional = medicineRepository.findById(detail.getMedicineId());
                                    if(medicineOptional.isEmpty()){
                                        errorField.put(String.format("details[%s].medicineId", index.get()), MEDICINE_EXCEPTION.MEDICINE_NOT_FOUND);
                                        throw new ObjectIllegalArgumentException(errorField, MEDICINE_EXCEPTION.FAIL_VALIDATION_PATIENT_MEDICINE_INVOICE);
                                    }

                                    Medicine medicine = medicineOptional.get();
                                    if(medicine.getQuantity() < detail.getQuantity()){
                                        errorField.put(String.format("details[%s].quantity", index.get()), MEDICINE_EXCEPTION.QUANTITY_REQUEST_EXCEED_STOCK);
                                        throw new ObjectIllegalArgumentException(errorField, MEDICINE_EXCEPTION.FAIL_VALIDATION_PATIENT_MEDICINE_INVOICE);
                                    }

                                    medicine.subtractQuantity(detail.getQuantity());
                                    medicines.add(medicine);
                                    index.incrementAndGet();

                                    return ExportInvoiceDetail.builder()
                                            .createdAt(LocalDateTime.now())
                                            .invoiceId(request.getInvoiceId())
                                            .medicine(medicine)
                                            .price(medicine.getPrice())
                                            .build();
                                }).toList();

        exportInvoiceDetailRepository.saveAll(details);
        medicineRepository.saveAll(medicines);
    }

    @Transactional
    public MedicineResponse save(CreateMedicineRequest request){
        Medicine medicine = medicineMapperUtil.mapCreateRequestToEntity(request);
        medicine.setQuantity(0);
        medicine = medicineRepository.save(medicine);
        return medicineMapperUtil.mapEntityToResponse(medicine);
    }

    @Transactional
    public MedicineResponse update(UpdateMedicineRequest request, UUID id){
        Medicine medicine = medicineRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(MEDICINE_EXCEPTION.MEDICINE_NOT_FOUND));
        medicineMapperUtil.mapUpdateRequestToEntity(request, medicine);
        medicine = medicineRepository.save(medicine);
        return medicineMapperUtil.mapEntityToResponse(medicine);
    }

    @Transactional
    @KafkaListener(topics = KafkaTopicsConstrant.PAY_MEDICINE_IN_CASH, groupId = KafkaTopicsConstrant.MEDICINE_GROUP)
    public void handlePayMedicineInCash(PayMedicineInCashCommonRequest request){
        List<UUID> medicineIds = request.getDetails().stream().map(PayMedicineDetailCommonRequest::getMedicineId).toList();
        Map<UUID, Medicine> medicines = medicineRepository.findAllById(medicineIds).stream().collect(Collectors.toMap(Medicine::getId, v -> v));

        List<PaidMedicineDetailCommonResponse> detailsResponse = new ArrayList<>();
        for(PayMedicineDetailCommonRequest detail: request.getDetails()){
            Medicine medicine = medicines.get(detail.getMedicineId());
            medicine.setQuantity(medicine.getQuantity() - detail.getQuantity());

            PaidMedicineDetailCommonResponse medicineResponse = PaidMedicineDetailCommonResponse.builder()
                    .medicineId(medicine.getId())
                    .quantity(detail.getQuantity())
                    .price(medicine.getPrice())
                    .build();
            detailsResponse.add(medicineResponse);
        }

        PaidMedicineCommonResponse paidInvoiceResponse = PaidMedicineCommonResponse.builder()
                                                                .invoiceId(request.getInvoiceId())
                                                                .details(detailsResponse)
                                                                .build();
        kafkaTemplate.send(paidMedicineInvoiceTopic, paidInvoiceResponse);
    }

    public BigDecimal calculateMedicinesCost(List<PayMedicineDetailCommonRequest> requests){
        List<UUID> medicineIds = requests.stream().map(PayMedicineDetailCommonRequest::getMedicineId).toList();
        Map<UUID, Medicine> medicineMap = medicineRepository.findAllById(medicineIds).stream().collect(Collectors.toMap(k -> k.getId(), v -> v));
        BigDecimal cost = BigDecimal.ZERO;
        for(PayMedicineDetailCommonRequest request: requests){
            BigDecimal medicineCost = medicineMap.get(request.getMedicineId()).getPrice().multiply(new BigDecimal(request.getQuantity()));
            cost = cost.add(medicineCost);
        }

        return cost;
    }
}