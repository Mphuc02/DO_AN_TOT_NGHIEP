package dev.examinationresult.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateExaminationResultRequest {
    @NotEmpty(message = "Chi tiết khám không đươc bỏ trống")
    private List<CreateExaminationResultDetailRequest> details;
}