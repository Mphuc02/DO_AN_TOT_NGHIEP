package dev.authentication.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterAccountRequest {
    @NotEmpty(message = "Tài khoản không được bỏ trống")
    private String userName;
    @NotEmpty(message = "Mật khẩu không được bỏ trống")
    private String passWord;
    @NotEmpty(message = "Số điện thoại không được bỏ trống")
    private String numberPhone;
    @NotEmpty(message = "Email không được bỏ trống")
    private String email;
}