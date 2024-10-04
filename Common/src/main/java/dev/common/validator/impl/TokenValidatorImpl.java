package dev.common.validator.impl;

import dev.common.constant.ExceptionConstant;
import dev.common.exception.JwtException;
import dev.common.exception.NotPermissionException;
import dev.common.model.TokenType;
import dev.common.validator.TokenValidator;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@RequiredArgsConstructor
public class TokenValidatorImpl implements ConstraintValidator<TokenValidator, String> {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.access-token=expiration}")
    public long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.refresh-token-expiration}")
    public long REFRESH_TOKEN_EXPIRATION;

    private TokenType tokenType;

    @Override
    public void initialize(TokenValidator constraintAnnotation) {
        this.tokenType = constraintAnnotation.tokenType();
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(token))
            return true;

        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(SECRET_KEY)
                                .parseClaimsJws(token)
                                .getBody();

            long timeExpiration = ACCESS_TOKEN_EXPIRATION;
            if(tokenType.equals(TokenType.REFRESH_TOKEN))
                timeExpiration = REFRESH_TOKEN_EXPIRATION;

            Date issuedAt = claims.getIssuedAt();
            Date expiredAt = claims.getExpiration();

            return expiredAt.getTime() == (issuedAt.getTime() + timeExpiration);
        }catch (ExpiredJwtException ex){
            throw new JwtException("Expired JWT token");
        }
        catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
}