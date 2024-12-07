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
public class CreateNewPatientCommonRequest {
    @Setter(AccessLevel.NONE)
    private UUID id = UUID.randomUUID();

    @NotNull(message = "Họ và tên không được bỏ trống")
    @Valid
    private CreateWithFullNameCommonRequest fullName;

    @NotEmpty(message = "Số điện thoại không được bỏ trống")
    private String numberPhone;

    @Valid
    @NotNull(message = "Địa chỉ không được bỏ trống")
    @AddressValidator
    private CreateWithAddressCommonRequest address;

    private UUID examinationFormID;
}