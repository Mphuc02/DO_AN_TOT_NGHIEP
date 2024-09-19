package dev.medicine.dto.response;

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
    private UUID provinceId;
    private UUID districtId;
    private UUID communeId;
}