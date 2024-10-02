package dev.medicine.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.BadRequestException;
import dev.common.exception.NotFoundException;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.medicine.dto.request.create.CreateMedicineRequest;
import dev.medicine.dto.request.create.CreatePatientMedicineInvoiceRequest;
import dev.medicine.dto.request.update.UpdateMedicineRequest;
import dev.medicine.dto.response.MedicineResponse;
import dev.medicine.entity.ExportInvoiceDetail;
import dev.medicine.entity.Medicine;
import dev.medicine.repository.ExportInvoiceDetailRepository;
import dev.medicine.repository.MedicineRepository;
import dev.medicine.util.MedicineMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

    private KafkaTemplate<String, Object> kafkaTemplate;

    public MedicineResponse getByID(UUID id){
        Medicine medicine = medicineRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException(MEDICINE_EXCEPTION.MEDICINE_NOT_FOUND));
        return medicineMapperUtil.mapEntityToResponse(medicine);
    }

    public List<MedicineResponse> getAll(){
        return medicineMapperUtil.mapEntitiesFromResponses(medicineRepository.findAll());
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
                                }).collect(Collectors.toList());

        exportInvoiceDetailRepository.saveAll(details);
        medicineRepository.saveAll(medicines);
    }

    @Transactional
    public MedicineResponse save(CreateMedicineRequest request){
        Medicine medicine = medicineMapperUtil.mapCreateRequestToEntity(request);
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
}