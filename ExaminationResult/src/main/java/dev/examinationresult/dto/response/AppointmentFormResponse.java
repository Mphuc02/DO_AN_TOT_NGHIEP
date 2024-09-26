package dev.examinationresult.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentFormResponse {
    private UUID id;
    private UUID workingScheduleId;
    private UUID employeeId;
    private UUID patientId;
    private LocalDateTime createdAt;
    private LocalDate appointingAt;
}