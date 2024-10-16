package dev.common.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationFormResponse {
    private UUID id;
    private UUID patientId;
    private UUID employeeId;
    private Integer ticketIndex;
    private LocalDateTime createdAt;
    private String symptom;
}