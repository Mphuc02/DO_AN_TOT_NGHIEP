package dev.examinationresult.dto.response;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationResultDetailResponse {
    private UUID id;
    private UUID diseaseId;
    private String diseaseDescription;
}