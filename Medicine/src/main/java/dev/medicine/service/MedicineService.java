package dev.medicine.service;

import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.NotFoundException;
import dev.medicine.dto.request.create.CreateMedicineRequest;
import dev.medicine.dto.request.update.UpdateMedicineRequest;
import dev.medicine.dto.response.MedicineResponse;
import dev.medicine.entity.Medicine;
import dev.medicine.repository.MedicineRepository;
import dev.medicine.util.MedicineMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineMapperUtil medicineMapperUtil;

    public List<MedicineResponse> getAll(){
        return medicineMapperUtil.mapEntitiesFromResponses(medicineRepository.findAll());
    }

    public List<UUID> checkMedicinesExist(List<UUID> ids){
        return medicineRepository.findAllById(ids)
                            .stream()
                            .map(Medicine::getId)
                            .toList();
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