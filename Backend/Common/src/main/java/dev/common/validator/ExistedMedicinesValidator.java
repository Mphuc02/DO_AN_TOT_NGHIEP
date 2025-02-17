package dev.common.validator;

import dev.common.validator.impl.ExistedMedicinesValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistedMedicinesValidatorImpl.class)
public @interface ExistedMedicinesValidator {
    String message() default "Các thuốc sau đây không tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
