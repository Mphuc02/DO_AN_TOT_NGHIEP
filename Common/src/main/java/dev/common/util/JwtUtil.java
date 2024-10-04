package dev.common.util;

import dev.common.constant.ValueConstant.*;
import dev.common.model.AuthenticatedUser;
import dev.common.model.Permission;
import dev.common.model.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {
    @Value(JWT.SECRET_KEY)
    private String SECRET_KEY;

    @Value(JWT.ACCESS_TOKEN_EXPIRATION)
    public int ACCESS_TOKEN_EXPIRATION;

    @Value(JWT.REFRESH_TOKEN_EXPIRATION)
    public int REFRESH_TOKEN_EXPIRATION;

    public Claims getClaims(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid JWT token");
        } catch (SignatureException | IllegalArgumentException e) {
            throw new JwtException("Jwt không hợp lệ");
        }
    }

    public AuthenticatedUser getUserFromJwt(String token, TokenType tokenType) {
        Claims claims = getClaims(token);

        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();

        long timeExpiration = ACCESS_TOKEN_EXPIRATION;
        if(tokenType.equals(TokenType.REFRESH_TOKEN))
            timeExpiration = REFRESH_TOKEN_EXPIRATION;
        if(expiration.getTime() != issuedAt.getTime() + timeExpiration)
            throw new JwtException("Jwt không hợp lệ");

        List<String> roles = (List<String>) claims.get("roles");
        Set<Permission> permissions = roles.stream().map(role -> Permission.valueOf(role)).collect(Collectors.toSet());
        UUID id = UUID.fromString(String.valueOf(claims.get("userId")));
        return AuthenticatedUser.builder()
                .id(id)
                .permissions(permissions)
                .build();
    }

    public UUID getTokenId(String jwt){
        Claims claims = getClaims(jwt);
        return UUID.fromString(claims.getSubject());
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtException("Unsupported JWT token");
        }
//        catch (IllegalArgumentException ex) {
//            throw new JwtException("JWT claims string is empty.");
//        }
    }
}