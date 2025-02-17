package dev.medicine.dto.request.create;

import dev.medicine.dto.request.save.SaveAddressRequest;
import dev.medicine.validator.AddressValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateSupplierRequest {
    @NotEmpty(message = "Tên nhà cung cấp không được bỏ trống")
    private String name;

    @NotEmpty(message = "Mô tả không được bỏ trống")
    private String description;

    @Valid
    @NotNull(message = "Địa chỉ không được bỏ trống")
    @AddressValidator
    private SaveAddressRequest address;
}