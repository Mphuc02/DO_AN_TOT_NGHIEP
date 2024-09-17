package dev.common.dto.response;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DiseaseCommonResponse {
    private UUID id;
    private String name;
    private String description;
}