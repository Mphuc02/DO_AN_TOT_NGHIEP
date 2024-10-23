package dev.patient.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {
    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private String description;
    private List<AppointmentDetailResponse> details;
    private List<AppointmentImageDetailResponse> images;
    private LocalDate appointmentDate;
    private LocalDateTime createdAt;
}