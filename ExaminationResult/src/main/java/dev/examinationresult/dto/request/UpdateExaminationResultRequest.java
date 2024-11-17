package dev.examinationresult.dto.request;

import dev.examinationresult.validor.DiseasesExistValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateExaminationResultRequest {
    private UUID patientId;

    @NotEmpty(message = "Lời khuyên không được bỏ trống")
    private String treatment;

    @Valid
    @NotEmpty(message = "Chi tiết khám không đươc bỏ trống")
    @DiseasesExistValidator
    private List<CreateExaminationResultDetailRequest> details;
}