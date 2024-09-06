package dev.greeting.dto.request;

import dev.common.dto.request.CreateFullNameRequest;
import dev.common.dto.request.CreatePatientAddressRequest;
import dev.common.validation.PatientAddressValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewPatientRequest {
    @NotNull(message = "Họ và tên không được bỏ trống")
    @Valid
    private CreateFullNameRequest fullName;

    @NotEmpty(message = "Số điện thoại không được bỏ trống")
//    @
    private String numberPhone;

    @Valid
    @NotNull(message = "Địa chỉ không được bỏ trống")
    @PatientAddressValidation
    private CreatePatientAddressRequest address;
}