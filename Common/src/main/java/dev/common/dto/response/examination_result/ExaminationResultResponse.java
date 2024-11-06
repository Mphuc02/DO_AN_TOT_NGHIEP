package dev.common.dto.response.examination_result;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationResultResponse {
    private UUID id;
    private UUID patientId;
    private UUID workingScheduleId;
    private LocalDateTime createdAt;
    private String treatment;
    private UUID appointmentId;
    private LocalDateTime examinatedAt;
    private Integer examinedNumber;
    private List<ExaminationResultDetailResponse> details;
}