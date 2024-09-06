package dev.greeting.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateFormWithoutAppointmentRequest {
    @NotNull(message = "Thông tin bệnh nhân không được bỏ trống")
    @Valid
    private NewPatientRequest patient;

    @NotNull(message = "Số thứ tự không được bỏ trống")
    @Min(value = 1, message = "Số thứ tự phải bắt đầu từ 1")
    private Integer numberCall;

    @NotEmpty(message = "Triệu chứng không được bỏ trống")
    private String symptom;
}