package dev.payment.dto.request;

import dev.payment.validator.AppliedDateValidator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateExaminationCostRequest {
    @NotNull(message = "Gía khám không được bỏ trống")
    @Min(value = 0, message = "Gía khám bệnh phải lớn hơn bằng 0")
    private BigDecimal cost;

    @NotNull(message = "Ngày áp dụng không được bỏ trống")
    @AppliedDateValidator
    private LocalDate appliedAt;
}