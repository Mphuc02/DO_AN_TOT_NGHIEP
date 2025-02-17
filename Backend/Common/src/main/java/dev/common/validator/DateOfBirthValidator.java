package dev.common.validator;

import dev.common.validator.impl.DateOfBirthValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateOfBirthValidatorImpl.class)
public @interface DateOfBirthValidator {
    int minimumAge() default 0;
    String message() default "Tuổi phải ít nhất là 18";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}