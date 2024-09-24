package dev.examinationresult.dto.request;

import dev.common.validator.DateAfterTodayValidator;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateAppointmentFormRequest {
    @NotNull(message = "Bệnh nhân không được bỏ trống")
    private UUID patientId;

    @DateAfterTodayValidator
    @NotNull(message = "Ngày hẹn khám lại không được bỏ trống")
    private LocalDate appointingAt;
}