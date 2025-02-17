package dev.common.dto.response.examination_result;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicineConsultationFormDetailResponse {
    private UUID id;
    private UUID medicineId;
    private Integer quantity;
    private String treatment;
}