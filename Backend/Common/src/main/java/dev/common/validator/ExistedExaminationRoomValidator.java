package dev.common.validator;

import dev.common.validator.impl.ExistedExaminationRoomValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistedExaminationRoomValidatorImpl.class)
public @interface ExistedExaminationRoomValidator {
    String message() default "Phòng với id không tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}