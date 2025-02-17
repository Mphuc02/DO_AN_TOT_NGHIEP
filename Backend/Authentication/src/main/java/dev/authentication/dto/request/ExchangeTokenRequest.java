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
public class ExchangeTokenRequest {
    @NotEmpty(message = "Token không được bỏ trống")
    @TokenValidator(message = "Refresh token không hợp lệ", tokenType = TokenType.REFRESH_TOKEN)
    private String token;
}