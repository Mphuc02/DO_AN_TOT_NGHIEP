package dev.examinationresult.dto.request;

import dev.examinationresult.validor.DiseasesExistValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateExaminationResultRequest {
    @Valid
    @NotEmpty(message = "Chi tiết khám không đươc bỏ trống")
    @DiseasesExistValidator
    private List<CreateExaminationResultDetailRequest> details;
}