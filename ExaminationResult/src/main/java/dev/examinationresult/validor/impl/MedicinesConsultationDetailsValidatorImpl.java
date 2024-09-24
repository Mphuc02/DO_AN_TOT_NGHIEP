package dev.examinationresult.validor.impl;

import dev.common.client.MedicineClient;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.ObjectIllegalArgumentException;
import dev.examinationresult.dto.request.SaveMedicineConsultationFormDetailRequest;
import dev.examinationresult.validor.MedicineConsultationDetailsValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import java.util.*;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class MedicinesConsultationDetailsValidatorImpl implements ConstraintValidator<MedicineConsultationDetailsValidator, List<SaveMedicineConsultationFormDetailRequest>> {
    private final MedicineClient client;

    @Override
    public boolean isValid(List<SaveMedicineConsultationFormDetailRequest> requests, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(requests))
            return true;

        Map<Integer, HashMap<String, String>> errors = new HashMap<>();
        List<UUID> medicineIds = new ArrayList<>();

        IntStream.range(0, requests.size()).forEach(index -> {
            HashMap<String, String> errorDetail = new HashMap<>();
            if(requests.get(index).getMedicineId() == null){
                errorDetail.put("medicineId", "Thuốc không được bỏ trống");
            }else {
                medicineIds.add(requests.get(index).getMedicineId());
            }

            if(requests.get(index).getQuantity() == null){
                errorDetail.put("quantity", "Số lượng không được bỏ trống");
            }
            else {
                if(requests.get(index).getQuantity() <= 0)
                    errorDetail.put("quantity", "Số lượng phải lớn hơn bằng 1");
            }

            if(ObjectUtils.isEmpty(requests.get(index).getTreatment()))
                errorDetail.put("treatment", "Cách dùng không được bỏ trống");

            if(!errorDetail.isEmpty())
                errors.put(index, errorDetail);
        });

        Set<UUID> resultIds = client.checkMedicinesExist(medicineIds);
        IntStream.range(0, medicineIds.size()).forEach(index -> {
            if(!resultIds.contains(medicineIds.get(index))){
                HashMap<String, String> errorDetail = errors.computeIfAbsent(index, k -> new HashMap<>());
                errorDetail.put("medicineId", "Không tồn tại thuốc với id này");
            }
        });

        if(!errors.isEmpty())
            throw new ObjectIllegalArgumentException((Map<Object, Object>) (Object)errors,
                                        EXAMINATION_RESULT_EXCEPTION.FAIL_VALIDATION_CONSULTATION_DETAIL);

        return true;
    }
}