package dev.medicine.validator.impl;

import dev.medicine.repository.SupplierRepository;
import dev.medicine.validator.ExistedSupplierValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RequiredArgsConstructor
public class ExistedSupplierValidatorImpl implements ConstraintValidator<ExistedSupplierValidator, UUID> {
    private final SupplierRepository repository;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext constraintValidatorContext) {
        return id != null && repository.existsById(id);
    }
}
