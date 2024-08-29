package dev.employee.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Valid
    private CreateFullNameRequest fullName;

    @Valid
    private CreateEmployeePermission permission;
}