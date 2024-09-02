package dev.common.validation.impl;

import dev.common.validation.DateOfBirthValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Date;

public class DateOfBirthValidationImpl implements ConstraintValidator<DateOfBirthValidation, Date> {
    int minimumAge;

    @Override
    public void initialize(DateOfBirthValidation constraintAnnotation) {
        this.minimumAge = constraintAnnotation.minimumAge();
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if(date == null)
            return true;

        Date today = new Date();
        return date.getYear() <= (today.getYear() - minimumAge + 1);
    }
}