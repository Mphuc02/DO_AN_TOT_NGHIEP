package dev.authentication.dto.request;

import dev.common.model.TokenType;
import dev.common.validator.TokenValidator;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogoutRequest {
    @TokenValidator(message = "Access token không hợp lệ")
    @NotEmpty(message = "Access token không được bỏ trống")
    private String accessToken;

    @TokenValidator(message = "Refresh token không hợp lệ", tokenType = TokenType.REFRESH_TOKEN)
    @NotEmpty(message = "Refresh token không được bỏ trống")
    private String refreshToken;
}