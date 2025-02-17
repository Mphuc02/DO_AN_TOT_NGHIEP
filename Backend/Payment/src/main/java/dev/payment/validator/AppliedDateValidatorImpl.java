package dev.payment.validator;

import dev.common.constant.ExceptionConstant;
import dev.common.exception.BadRequestException;
import dev.payment.service.ExaminationCostService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@RequiredArgsConstructor
public class AppliedDateValidatorImpl implements ConstraintValidator<AppliedDateValidator, LocalDate> {
    private final ExaminationCostService costService;

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if(date == null)
            return true;

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        if(date.isBefore(tomorrow)){
            throw new BadRequestException(ExceptionConstant.PAYMENT_EXCEPTION.APPLIED_DAY_MUST_AFTER_TODAY);
        }
        return !costService.existByAppliedDate(date);
    }
}