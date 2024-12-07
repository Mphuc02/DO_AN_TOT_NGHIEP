package dev.common.validator.impl;

import dev.common.client.InternalAuthenticationClient;
import dev.common.dto.request.CreateNewPatientCommonRequest;
import dev.common.validator.NewPatientValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class NewPatientValidatorImpl implements ConstraintValidator<NewPatientValidator, CreateNewPatientCommonRequest> {
    private final InternalAuthenticationClient client;

    @Override
    public boolean isValid(CreateNewPatientCommonRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if(request == null || ObjectUtils.isEmpty(request.getNumberPhone()))
            return true;

        return client.checkPhoneNumberNotExist(request.getNumberPhone(), request.getId());
    }
}