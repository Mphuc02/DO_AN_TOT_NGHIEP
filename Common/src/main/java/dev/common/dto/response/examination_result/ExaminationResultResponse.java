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
    private LocalDateTime examinatedAt;
    private Integer orderNumber;
    private List<ExaminationResultDetailResponse> details;
}