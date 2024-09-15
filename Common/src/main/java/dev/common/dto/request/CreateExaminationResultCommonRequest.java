package dev.common.dto.request;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateExaminationResultCommonRequest {
    private UUID id;
    private UUID patientId;
    private UUID workingScheduleId;
    private Integer orderNumber;
}