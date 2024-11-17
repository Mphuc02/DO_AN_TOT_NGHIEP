package dev.common.dto.request;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateRelationShipCommonRequest {
    private UUID doctorId;
    private UUID patientId;
}