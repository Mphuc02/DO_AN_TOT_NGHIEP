package dev.common.validator.impl;

import dev.common.validator.DateOfBirthValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Date;

public class DateOfBirthValidatorImpl implements ConstraintValidator<DateOfBirthValidator, Date> {
    int minimumAge;

    @Override
    public void initialize(DateOfBirthValidator constraintAnnotation) {
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