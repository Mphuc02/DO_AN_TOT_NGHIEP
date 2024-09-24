package dev.common.validator;

import dev.common.validator.impl.ExistedPatientValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistedPatientValidatorImpl.class)
public @interface ExistedPatientValidator {
    String message() default "Các thuốc sau đây không tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}