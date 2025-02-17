package dev.examinationresult.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateExaminationResultDetailRequest {
    @NotNull(message = "Tên bệnh không được bỏ trống")
    private UUID diseaseId;

    @NotEmpty(message = "Mô tả của bệnh không được bỏ trống")
    private String diseaseDescription;
}