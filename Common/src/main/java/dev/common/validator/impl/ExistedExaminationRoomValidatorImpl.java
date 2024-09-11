package dev.common.validator.impl;

import dev.common.client.ExaminationRoomClient;
import dev.common.validator.ExistedExaminationRoomValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import java.util.UUID;

@RequiredArgsConstructor
public class ExistedExaminationRoomValidatorImpl implements ConstraintValidator<ExistedExaminationRoomValidator, UUID> {
    private final ExaminationRoomClient client;

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(uuid))
            return true;
        return client.checkRoomExist(uuid);
    }
}
