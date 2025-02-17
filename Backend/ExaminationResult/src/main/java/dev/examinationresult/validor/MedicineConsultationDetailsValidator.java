package dev.examinationresult.validor;

import dev.examinationresult.validor.impl.MedicinesConsultationDetailsValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MedicinesConsultationDetailsValidatorImpl.class)
public @interface MedicineConsultationDetailsValidator {
    String message() default "Lỗi khi kiểm tra chi tiết phiếu phát thuốc";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}