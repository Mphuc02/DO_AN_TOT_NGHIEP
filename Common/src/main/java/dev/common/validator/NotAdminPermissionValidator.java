package dev.common.validator;

import dev.common.validator.impl.NotAdminPermissionValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotAdminPermissionValidatorImpl.class)
public @interface NotAdminPermissionValidator {
    String message() default "Không thể thao tác với chức vụ naày";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}