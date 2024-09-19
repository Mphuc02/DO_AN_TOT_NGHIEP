package dev.common.dto.request;

import dev.common.validator.AddressValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateNewPatientRequest {
    private UUID id;

    @NotNull(message = "Họ và tên không được bỏ trống")
    @Valid
    private CreateFullNameRequest fullName;

    @NotEmpty(message = "Số điện thoại không được bỏ trống")
    private String numberPhone;

    @Valid
    @NotNull(message = "Địa chỉ không được bỏ trống")
    @AddressValidator
    private CreatePatientAddressRequest address;

    private UUID examinationFormID;
}