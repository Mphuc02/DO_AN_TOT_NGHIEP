package dev.common.validation;

import dev.common.validation.impl.NotAdminPermissionValidationImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotAdminPermissionValidationImpl.class)
public @interface NotAdminPermissionValidation {
    String message() default "Không thể thao tác với chức vụ naày";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}