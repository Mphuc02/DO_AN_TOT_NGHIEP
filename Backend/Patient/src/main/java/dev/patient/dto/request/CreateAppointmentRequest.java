package dev.patient.dto.request;

import dev.common.model.Role;
import dev.common.validator.DateAfterTodayValidator;
import dev.common.validator.ExistedDiseasesValidator;
import dev.common.validator.ExistedEmployeeValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class CreateAppointmentRequest {
    @ExistedEmployeeValidator(message = "Bác sĩ không tồn tại", permissions = Role.DOCTOR)
    @NotNull(message = "Bác sĩ không được bỏ trống")
    private UUID doctorId;

    @DateAfterTodayValidator
    @NotNull(message = "Ngày hẹn khám không được bỏ trống")
    private LocalDate appointmentDate;

    @NotEmpty(message = "Ghi chú không được bỏ trống")
    private String description;

    @ExistedDiseasesValidator(field = Fields.diseasesIds)
    private List<UUID> diseasesIds;

    @Valid
    private List<CreateAppointmentImageDetailRequest> images;
}