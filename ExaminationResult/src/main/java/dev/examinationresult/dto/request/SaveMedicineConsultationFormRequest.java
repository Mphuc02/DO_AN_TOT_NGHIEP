package dev.examinationresult.dto.request;

import dev.examinationresult.validor.MedicineConsultationDetailsValidator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveMedicineConsultationFormRequest {
    @NotNull(message = "Kết quả khám không được bỏ trống")
    private UUID id;

    @NotEmpty(message = "Chi tiết phiếu thuốc không được bỏ trống")
    @MedicineConsultationDetailsValidator
    private List<SaveMedicineConsultationFormDetailRequest> details;
}