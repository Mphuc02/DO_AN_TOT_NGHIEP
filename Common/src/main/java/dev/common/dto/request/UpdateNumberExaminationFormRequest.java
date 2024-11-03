package dev.common.dto.request;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateNumberExaminationFormRequest {
    private UUID id;
    private Integer examinedNumber;
}