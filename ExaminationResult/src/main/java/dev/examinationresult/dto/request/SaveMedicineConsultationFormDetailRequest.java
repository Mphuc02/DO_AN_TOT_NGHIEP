package dev.examinationresult.dto.request;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveMedicineConsultationFormDetailRequest {
    private UUID medicineId;
    private Integer quantity;
    private String treatment;
}