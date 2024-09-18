package dev.medicine.validator;

import dev.medicine.validator.impl.ExistedOriginValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistedOriginValidatorImpl.class)
public @interface ExistedOriginValidator {
    String message() default "Xuất sứ không tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}