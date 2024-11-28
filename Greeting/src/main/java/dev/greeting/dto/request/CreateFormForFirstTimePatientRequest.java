package dev.greeting.dto.request;

import dev.common.dto.request.CreateNewPatientRequest;
import dev.common.validator.NewPatientValidator;
import dev.common.validator.TodayWorkingScheduleValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateFormForFirstTimePatientRequest {
    @NotNull(message = "Thông tin bệnh nhân không được bỏ trống")
    @Valid
    @NewPatientValidator(message = "Số điện thoại đã được đăng ký")
    private CreateNewPatientRequest patient;

    @NotEmpty(message = "Triệu chứng không được bỏ trống")
    private String symptom;

    @NotNull(message = "Phòng khám không được bỏ trống")
    @TodayWorkingScheduleValidator
    private UUID workingSchedule;

    private UUID owner;
}