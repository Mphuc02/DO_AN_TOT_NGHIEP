package dev.common.validator.impl;

import dev.common.client.DiseaseClient;
import dev.common.constant.ExceptionConstant.*;
import dev.common.exception.NotFoundException;
import dev.common.validator.DiseasesExistValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DiseasesExistValidatorImpl implements ConstraintValidator<DiseasesExistValidator, List<UUID>> {
    private final DiseaseClient diseaseClient;

    @Override
    public boolean isValid(List<UUID> ids, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(ids))
            return true;
        List<UUID> response = diseaseClient.checkDiseasesExist(ids);
        if(response.size() == ids.size())
            return true;

        List<UUID> idsNotFound = ids.stream().filter(id -> !response.contains(id)).toList();
        throw new NotFoundException(idsNotFound, HOSPITAL_INFORMATION_EXCEPTION.DISEASE_NOT_FOUND);
    }
}