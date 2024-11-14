package dev.patient.dto.request;

import dev.common.validator.DateAfterTodayValidator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class CreateAppointmentByDoctorRequest {
    @NotNull(message = "Bệnh nhân không được bỏ trống")
    private UUID patientId;

    @NotNull(message = "Kết quả khám không được bỏ trống")
    private UUID examinationResultId;

    @NotNull(message = "Lý do không được bỏ trống")
    private String description;

    @DateAfterTodayValidator
    @NotNull(message = "Ngày hẹn không được bỏ trống")
    private LocalDate appointmentDate;
}