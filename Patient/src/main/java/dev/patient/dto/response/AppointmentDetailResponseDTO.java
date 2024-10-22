package dev.patient.dto.response;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentDetailResponseDTO {
    private UUID id;
    private UUID diseaseId;
}