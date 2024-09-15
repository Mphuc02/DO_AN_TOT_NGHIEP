package dev.common.validator.impl;

import dev.common.client.InternalAuthenticationClient;
import dev.common.dto.request.CreateNewPatientRequest;
import dev.common.validator.NewPatientValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@RequiredArgsConstructor
public class NewPatientValidatorImpl implements ConstraintValidator<NewPatientValidator, CreateNewPatientRequest> {
    private final InternalAuthenticationClient client;

    @Override
    public boolean isValid(CreateNewPatientRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if(request == null || ObjectUtils.isEmpty(request.getNumberPhone()))
            return true;

        request.setId(UUID.randomUUID());
        return client.checkPhoneNumberNotExist(request.getNumberPhone(), request.getId());
    }
}