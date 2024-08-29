package dev.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommonRegisterAccountRequest {
    @NotEmpty(message = "Tài khoản không được bỏ trống")
    @Length(min = 7, max = 20, message = "Tài khoản có độ dài từ 7 đến 20 ký tự")
    private String userName;

    @NotEmpty(message = "Mật khẩu không được bỏ trống")
    @Length(min = 7, max = 20, message = "Mật khẩu có độ dài từ 7 đên 20 ký tự")
    private String passWord;

    @NotEmpty(message = "Số điện thoại không được bỏ trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải có 10 chữ số và bắt đầu bởi 0")
    private String numberPhone;

    @NotEmpty(message = "Email không được bỏ trống")
    @Email(message = "Email không đúng đinh dạng")
    private String email;
}