package dev.examinationresult.dto.request;

import dev.examinationresult.validor.MedicineConsultationDetailsValidator;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveMedicineConsultationFormRequest {
    @NotEmpty(message = "Chi tiết phiếu thuốc không được bỏ trống")
    @MedicineConsultationDetailsValidator
    private List<SaveMedicineConsultationFormDetailRequest> details;
}