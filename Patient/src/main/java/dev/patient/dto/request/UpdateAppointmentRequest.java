package dev.patient.dto.request;

import dev.common.model.Role;
import dev.common.validator.DateAfterTodayValidator;
import dev.common.validator.ExistedEmployeeValidator;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAppointmentRequest {
    @ExistedEmployeeValidator(message = "Bác sĩ không tồn tại", permissions = Role.DOCTOR)
    private UUID doctorId;

    @DateAfterTodayValidator
    private LocalDate appointmentDate;

    @NotEmpty(message = "Ghi chú không được bỏ trống")
    private String description;
}