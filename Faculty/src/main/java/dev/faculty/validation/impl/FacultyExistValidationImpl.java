package dev.faculty.validation.impl;

import dev.faculty.repository.FacultyRepository;
import dev.faculty.validation.FacultyExistValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@RequiredArgsConstructor
public class FacultyExistValidationImpl implements ConstraintValidator<FacultyExistValidation, UUID> {
    private final FacultyRepository facultyRepository;

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(uuid))
            return true;
        return facultyRepository.existsById(uuid);
    }
}
