package dev.examinationresult.validor.impl;

import dev.common.client.DiseaseClient;
import dev.common.constant.ExceptionConstant;
import dev.common.exception.NotFoundException;
import dev.examinationresult.dto.request.CreateExaminationResultDetailRequest;
import dev.examinationresult.validor.DiseasesExistValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DiseasesExistValidatorImpl implements ConstraintValidator<DiseasesExistValidator, List<CreateExaminationResultDetailRequest>> {
    private final DiseaseClient client;

    @Override
    public boolean isValid(List<CreateExaminationResultDetailRequest> requests, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(requests))
            return true;

        List<UUID> ids = requests.stream().map(request -> request.getDiseaseId()).collect(Collectors.toList());
        List<UUID> response = client.checkDiseasesExist(ids);
        if(response.size() == ids.size())
            return true;

        List<UUID> idsNotFound = ids.stream().filter(id -> !response.contains(id)).toList();
        throw new NotFoundException(idsNotFound, ExceptionConstant.HOSPITAL_INFORMATION_EXCEPTION.DISEASE_NOT_FOUND);
    }
}