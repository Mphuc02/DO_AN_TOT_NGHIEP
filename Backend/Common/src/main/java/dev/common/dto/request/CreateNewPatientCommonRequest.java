package dev.common.dto.request;

import dev.common.validator.AddressValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
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
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải có 10 chữ số và bắt đầu bởi 0")
    private String numberPhone;

    @NotNull(message = "Giới tính không được bỏ trống")
    private Integer gender;

    @NotNull(message = "Ngày sinh không được bỏ trống")
    private LocalDate dateOfBirth;

    @Valid
    @NotNull(message = "Địa chỉ không được bỏ trống")
    @AddressValidator
    private CreateWithAddressCommonRequest address;

    private UUID examinationFormID;
}