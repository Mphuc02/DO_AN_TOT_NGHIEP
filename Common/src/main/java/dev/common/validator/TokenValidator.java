package dev.common.validator;

import dev.common.model.TokenType;
import dev.common.validator.impl.TokenValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TokenValidatorImpl.class)
public @interface TokenValidator {
    TokenType tokenType() default TokenType.ACCESS_TOKEN;
    String message() default "Token không hợp lệ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
