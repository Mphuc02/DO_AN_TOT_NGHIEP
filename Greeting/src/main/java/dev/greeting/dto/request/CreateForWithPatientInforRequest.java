package dev.greeting.dto.request;

import dev.common.validator.ExistedPatientValidator;
import dev.common.validator.TodayWorkingScheduleValidator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CreateForWithPatientInforRequest {
    @NotNull(message = "Bệnh nhân không được bỏ trống")
    @ExistedPatientValidator
    private UUID patientId;

    @NotNull(message = "Số thứ tự không được bỏ trống")
    @Min(value = 1, message = "Số thứ tự phải bắt đầu từ 1")
    private Integer numberCall;

    @NotEmpty(message = "Triệu chứng không được bỏ trống")
    private String symptom;

    @NotNull(message = "Phòng khám không được bỏ trống")
    @TodayWorkingScheduleValidator
    private UUID workingScheduleId;
}