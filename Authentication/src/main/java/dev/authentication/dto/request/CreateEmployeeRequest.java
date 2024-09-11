package dev.authentication.dto.request;

import dev.common.dto.request.CreateFullNameRequest;
import dev.common.dto.request.CreatePermissionRequest;
import dev.common.validator.DateOfBirthValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateEmployeeRequest {
    @NotEmpty(message = "Giới thiệu về nhân viên không được bỏ trống")
    @Size(max = 1000, message = "Giới thiệu về nhân viên không được quá 1000 ký tự")
    private String introduce;

    @NotEmpty(message = "Số điện thoại không được bỏ trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải có 10 chữ số và bắt đầu bởi 0")
    private String numberPhone;

    @NotEmpty(message = "Email không được bỏ trống")
    @Email(message = "Email không đúng đinh dạng")
    private String email;

    @NotNull(message = "Ngày sinh không được bỏ trống")
    @DateOfBirthValidator(minimumAge = 18, message = "Tuổi phải ít nhất 18 tuổi")
    private Date dateOfBirth;

    @Valid
    private CreateFullNameRequest fullName;

    @Valid
    @NotEmpty(message = "Phải có ít nhất 1 quyền")
    private List<CreatePermissionRequest> permissions;
}