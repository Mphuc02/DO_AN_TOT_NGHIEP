package dev.greeting.dto.request;

import dev.common.dto.request.CreateNewPatientCommonRequest;
import dev.common.validator.NewPatientValidator;
import dev.common.validator.TodayWorkingScheduleValidator;
import jakarta.validation.Valid;
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
    private CreateNewPatientCommonRequest patient;

    @NotEmpty(message = "Triệu chứng không được bỏ trống")
    private String symptom;

    @NotNull(message = "Phòng khám không được bỏ trống")
    @TodayWorkingScheduleValidator
    private UUID workingScheduleId;

    private UUID examinationFormId;
    private UUID owner;
}