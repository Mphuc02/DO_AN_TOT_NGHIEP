package dev.common.validator;

import dev.common.model.Permission;
import dev.common.validator.impl.ExistedEmployeeValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistedEmployeeValidatorImpl.class)
public @interface ExistedEmployeeValidator {
    String message() default "Nhân viên với id không tồn tại";
    Permission permissions();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}