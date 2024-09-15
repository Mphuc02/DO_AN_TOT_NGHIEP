package dev.examinationresult.dto.request;

import jakarta.persistence.Column;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateExaminationResultDetailRequest {
    private UUID diseaseId;

    @Column(columnDefinition = "TEXT")
    private String diseaseDescription;
}