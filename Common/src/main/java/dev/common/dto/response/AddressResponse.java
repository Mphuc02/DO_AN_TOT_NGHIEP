package dev.common.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressResponse {
    private UUID id;
    private String street;
    private ProvinceResponse province;
    private DistrictResponse district;
    private CommuneResponse commune;
}