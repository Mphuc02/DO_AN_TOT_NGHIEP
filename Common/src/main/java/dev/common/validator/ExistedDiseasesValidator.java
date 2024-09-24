package dev.common.validator;

import dev.common.validator.impl.ExistedDiseasesValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistedDiseasesValidatorImpl.class)
public @interface ExistedDiseasesValidator {
    String message() default "Các bệnh sau đây không tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}