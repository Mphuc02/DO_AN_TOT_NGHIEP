package dev.common.dto.response.patient;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentDetailResponse {
    private UUID id;
    private UUID diseaseId;
}