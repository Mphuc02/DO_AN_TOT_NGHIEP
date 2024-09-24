package dev.patient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AddressResponse {
    private String street;
    private UUID provinceId;
    private UUID districtId;
    private UUID communeId;
}