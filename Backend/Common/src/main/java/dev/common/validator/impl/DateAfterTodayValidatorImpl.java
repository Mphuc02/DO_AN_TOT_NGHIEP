package dev.common.validator.impl;

import dev.common.validator.DateAfterTodayValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateAfterTodayValidatorImpl implements ConstraintValidator<DateAfterTodayValidator, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(date))
            return true;

        //Todo: Chỉnh lại sau hôm nay mới chuẩn
        LocalDate today = LocalDate.now().minusDays(1);
        return date.isAfter(today);
    }
}
