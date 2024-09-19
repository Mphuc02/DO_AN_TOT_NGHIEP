package dev.medicine.dto.request;

import dev.medicine.validator.AddressValidator;
import jakarta.validation.Valid;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateSupplierRequest {
    private String name;

    private String description;

    @Valid
    @AddressValidator
    private SaveAddressRequest address;
}