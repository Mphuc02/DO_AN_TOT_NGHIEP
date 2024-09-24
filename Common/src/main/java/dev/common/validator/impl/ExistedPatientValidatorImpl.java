package dev.common.validator.impl;

import dev.common.validator.ExistedPatientValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ExistedPatientValidatorImpl implements ConstraintValidator<ExistedPatientValidator, UUID> {

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}