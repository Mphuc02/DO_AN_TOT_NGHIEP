package dev.common.validator.impl;

import dev.common.client.PatientClient;
import dev.common.validator.ExistedPatientValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RequiredArgsConstructor
public class ExistedPatientValidatorImpl implements ConstraintValidator<ExistedPatientValidator, UUID> {
    private final PatientClient client;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext constraintValidatorContext) {
        return id == null ||
                client.checkPatientExist(id);
    }
}