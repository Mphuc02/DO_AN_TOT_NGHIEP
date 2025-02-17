package dev.medicine.validator.impl;

import dev.medicine.repository.OriginRepository;
import dev.medicine.validator.ExistedOriginValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import java.util.UUID;

@RequiredArgsConstructor
public class ExistedOriginValidatorImpl implements ConstraintValidator<ExistedOriginValidator, UUID> {
    private final OriginRepository originRepository;

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(uuid))
            return true;
        return originRepository.existsById(uuid);
    }
}