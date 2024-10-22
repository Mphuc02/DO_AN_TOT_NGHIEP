package dev.common.validator.impl;

import dev.common.client.DiseaseClient;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.BaseException;
import dev.common.model.ErrorField;
import dev.common.validator.ExistedDiseasesValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ExistedDiseasesValidatorImpl implements ConstraintValidator<ExistedDiseasesValidator, List<UUID>> {
    private final DiseaseClient diseaseClient;
    String field;

    @Override
    public void initialize(ExistedDiseasesValidator constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }

    private String elementAtIndex(int index){
        return String.format("%s[%s]", field, index);
    }

    @Override
    public boolean isValid(List<UUID> ids, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(ids))
            return true;
        List<UUID> response = diseaseClient.checkDiseasesExist(ids);
        if(response.size() == ids.size())
            return true;

        List<ErrorField> errorFields = new ArrayList<>();
        for(int index = 0; index < ids.size(); index++){
            if(!response.contains(ids.get(index))){
                errorFields.add(new ErrorField(HOSPITAL_INFORMATION_EXCEPTION.DISEASE_NOT_FOUND, elementAtIndex(index)));
            }
        }

        throw BaseException.buildBadRequest().addFields(errorFields).build();
    }
}