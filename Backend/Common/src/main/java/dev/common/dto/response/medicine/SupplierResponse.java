package dev.common.dto.response.medicine;

import dev.common.dto.response.address.AddressResponse;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SupplierResponse {
    private UUID id;
    private String name;
    private String description;
    private AddressResponse address;
}