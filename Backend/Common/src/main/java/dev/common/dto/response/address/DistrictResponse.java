package dev.common.dto.response.address;

import lombok.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DistrictResponse {
    private UUID id;
    private String name;
    private List<CommuneResponse> communes;
}