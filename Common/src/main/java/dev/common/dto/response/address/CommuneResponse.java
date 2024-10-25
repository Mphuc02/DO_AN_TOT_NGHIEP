package dev.common.dto.response.address;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommuneResponse {
    private UUID id;
    private String name;
}