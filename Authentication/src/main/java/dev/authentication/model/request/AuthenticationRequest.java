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
public class AuthenticationRequest {
    @NotEmpty(message = "Tài khoản không được bỏ trống")
    private String userName;
    @NotEmpty(message = "Mật khẩu không được bỏ trống")
    private String passWord;
}