package dev.common.validator.impl;

import dev.common.validator.ExistedMedicinesValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.UUID;

public class ExistedMedicinesValidatorImpl implements ConstraintValidator<ExistedMedicinesValidator, List<UUID>> {
    @Override
    public boolean isValid(List<UUID> uuids, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
