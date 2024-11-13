package dev.examinationresult.validor.impl;

import dev.common.client.MedicineClient;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.BaseException;
import dev.common.model.ErrorField;
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

        List<ErrorField> errorFields = new ArrayList<>();
        List<UUID> medicineIds = new ArrayList<>();

        IntStream.range(0, requests.size()).forEach(index -> {
            if(requests.get(index).getMedicineId() == null){
                ErrorField field = new ErrorField("Thuốc không được bỏ trống", String.format("details[%s].medicineId", index));
                errorFields.add(field);
            }else {
                medicineIds.add(requests.get(index).getMedicineId());
            }

            if(requests.get(index).getQuantity() == null){
                ErrorField field = new ErrorField("Số lượng không được bỏ trống", String.format("details[%s].quantity", index));
                errorFields.add(field);
            }
            else {
                if(requests.get(index).getQuantity() <= 0){
                    ErrorField field = new ErrorField("Số lượng phải lớn hơn bằng 1", String.format("details[%s].quantity", index));
                    errorFields.add(field);
                }
            }

            if(ObjectUtils.isEmpty(requests.get(index).getTreatment())){
                ErrorField field = new ErrorField("Cách dùng không được bỏ trống", String.format("details[%s].treatment", index));
                errorFields.add(field);
            }
        });

        Set<UUID> resultIds = client.checkMedicinesExist(medicineIds);
        IntStream.range(0, medicineIds.size()).forEach(index -> {
            if(!resultIds.contains(medicineIds.get(index))){
                ErrorField errorField = new ErrorField("Không tồn tại thuốc với id này", String.format("details[%s].medicineId", index));
                errorFields.add(errorField);
            }
        });

        if(!errorFields.isEmpty())
            throw BaseException.buildBadRequest().message(EXAMINATION_RESULT_EXCEPTION.FAIL_VALIDATION_CONSULTATION_DETAIL).addFields(errorFields).build();

        return true;
    }
}