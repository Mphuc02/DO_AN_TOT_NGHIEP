package dev.common.validator.impl;

import dev.common.client.WorkingScheduleClient;
import dev.common.validator.TodayWorkingScheduleValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RequiredArgsConstructor
public class TodayWorkingScheduleValidatorImpl implements ConstraintValidator<TodayWorkingScheduleValidator, UUID> {
    private final WorkingScheduleClient client;
    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null)
            return true;

        return client.checkScheduleIsToday(id);
    }
}
