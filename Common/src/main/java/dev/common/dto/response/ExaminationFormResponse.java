package dev.common.dto.response;

import lombok.*;
import java.sql.Date;
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
    private Date createdAt;
    private String symptom;
}