package dev.medicine.dto.response;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Getter
public class OriginResponse {
    private UUID id;
    private String name;
    private String description;
}