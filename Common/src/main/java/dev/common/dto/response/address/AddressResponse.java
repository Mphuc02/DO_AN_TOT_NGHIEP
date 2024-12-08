package dev.common.dto.response.address;

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
    private String provinceName;
    private UUID districtId;
    private String districtName;
    private UUID communeId;
    private String communeName
;}