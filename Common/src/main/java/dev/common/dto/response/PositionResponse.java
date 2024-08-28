package dev.common.dto.response;

import dev.common.model.Permission;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PositionResponse {
    private UUID id;
    private Integer quantity;
    private Permission permission;
}
