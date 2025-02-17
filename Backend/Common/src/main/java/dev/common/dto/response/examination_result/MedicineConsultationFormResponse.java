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
public class MedicineConsultationFormResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private UUID employeeId;
    private List<MedicineConsultationFormDetailResponse> details;
}