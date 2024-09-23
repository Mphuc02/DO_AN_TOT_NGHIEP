package dev.medicine.validator;

import dev.medicine.validator.impl.ExistedSupplierValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistedSupplierValidatorImpl.class)
public @interface ExistedSupplierValidator {
    String message() default "Nhà cung cấp không tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}