package dev.medicine.dto.response;

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