package dev.medicine.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.ExportMedicineCommonRequest;
import dev.common.dto.request.ExportMedicineDetailCommonRequest;
import dev.common.dto.response.medicine.PaidMedicineCommonResponse;
import dev.common.dto.response.medicine.PaidMedicineDetailCommonResponse;
import dev.common.dto.response.payment.CalculatedMedicinesCost;
import dev.common.exception.NotFoundException;
import dev.medicine.dto.request.create.CreateMedicineRequest;
import dev.medicine.dto.request.update.UpdateMedicineRequest;
import dev.common.dto.response.medicine.MedicineResponse;
import dev.medicine.entity.Medicine;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineMapperUtil medicineMapperUtil;
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
    public void handlePayMedicineInCash(ExportMedicineCommonRequest request){
        PaidMedicineCommonResponse response = handleSubtractQuantityMedicine(request);
        kafkaTemplate.send(paidMedicineInvoiceTopic, response);
    }

    @Transactional
    @KafkaListener(topics = KafkaTopicsConstrant.PAY_MEDICINE_ONLINE, groupId = KafkaTopicsConstrant.MEDICINE_GROUP)
    public void handlePayMedicineOnline(ExportMedicineCommonRequest request){
        PaidMedicineCommonResponse response = handleSubtractQuantityMedicine(request);
        response.setPaidOnline(true);
        kafkaTemplate.send(paidMedicineInvoiceTopic, response);
    }

    private PaidMedicineCommonResponse handleSubtractQuantityMedicine(ExportMedicineCommonRequest request){
        List<UUID> medicineIds = request.getDetails().stream().map(ExportMedicineDetailCommonRequest::getMedicineId).toList();
        Map<UUID, Medicine> medicines = medicineRepository.findAllById(medicineIds).stream().collect(Collectors.toMap(Medicine::getId, v -> v));

        List<PaidMedicineDetailCommonResponse> detailsResponse = new ArrayList<>();
        for(ExportMedicineDetailCommonRequest detail: request.getDetails()){
            Medicine medicine = medicines.get(detail.getMedicineId());
            medicine.setQuantity(medicine.getQuantity() - detail.getQuantity());

            PaidMedicineDetailCommonResponse medicineResponse = PaidMedicineDetailCommonResponse.builder()
                    .medicineId(medicine.getId())
                    .quantity(detail.getQuantity())
                    .price(medicine.getPrice())
                    .build();
            detailsResponse.add(medicineResponse);
        }
        return PaidMedicineCommonResponse.builder()
                .invoiceId(request.getInvoiceId())
                .details(detailsResponse)
                .build();
    }

    public CalculatedMedicinesCost calculateMedicinesCost(List<ExportMedicineDetailCommonRequest> requests){
        List<UUID> medicineIds = requests.stream().map(ExportMedicineDetailCommonRequest::getMedicineId).toList();
        Map<UUID, Medicine> medicineMap = medicineRepository.findAllById(medicineIds).stream().collect(Collectors.toMap(k -> k.getId(), v -> v));
        BigDecimal cost = BigDecimal.ZERO;

        CalculatedMedicinesCost calculatedMedicinesCost = new CalculatedMedicinesCost();
        List<PaidMedicineDetailCommonResponse> medicineDetails = new ArrayList<>();
        for(ExportMedicineDetailCommonRequest request: requests){
            Medicine medicine = medicineMap.get(request.getMedicineId());
            BigDecimal medicineCost = medicine.getPrice().multiply(new BigDecimal(request.getQuantity()));
            cost = cost.add(medicineCost);

            medicineDetails.add(PaidMedicineDetailCommonResponse.builder()
                    .medicineId(request.getMedicineId())
                    .quantity(request.getQuantity())
                    .price(medicine.getPrice())
                    .build());
        }

        calculatedMedicinesCost.setCost(cost);
        calculatedMedicinesCost.setDetails(medicineDetails);
        return calculatedMedicinesCost;
    }

    public List<MedicineResponse> getByIds(List<UUID> ids){
        return medicineMapperUtil.mapEntitiesFromResponses(medicineRepository.findAllById(ids));
    }
}