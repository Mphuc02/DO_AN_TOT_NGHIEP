package dev.common.dto.request;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetOrderNumberAppointmentRequest {
    private UUID appointmentId;
    private UUID doctorId;
}