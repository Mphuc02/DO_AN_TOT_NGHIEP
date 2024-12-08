package dev.hospitalinformation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAddressDetailRequest {
    private UUID provinceId;
    private UUID districtId;
    private UUID communeId;
}