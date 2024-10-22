package dev.patient.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentResponseDTO {
    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private String description;
    private List<AppointmentDetailResponseDTO> details;
    private LocalDate appointmentDate;
    private LocalDateTime createdAt;
}