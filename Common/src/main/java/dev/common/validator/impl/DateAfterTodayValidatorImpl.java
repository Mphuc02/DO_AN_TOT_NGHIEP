package dev.common.validator.impl;

import dev.common.validator.DateAfterTodayValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.util.Date;

public class DateAfterTodayValidatorImpl implements ConstraintValidator<DateAfterTodayValidator, Date> {
    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(date))
            return true;

        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        return date.after(today);
    }
}
