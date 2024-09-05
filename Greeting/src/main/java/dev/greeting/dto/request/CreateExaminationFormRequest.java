package dev.greeting.dto.request;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateExaminationFormRequest {
    private UUID patientId;
    private Integer numberCall;
    private String symptom;
}