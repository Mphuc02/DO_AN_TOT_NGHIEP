package dev.greeting.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CreateFormWithAppointmentRequest extends CreateForWithPatientInforRequest{
    @NotNull(message = "Lịch hẹn không được bỏ trống")
    private UUID appointmentId;
}