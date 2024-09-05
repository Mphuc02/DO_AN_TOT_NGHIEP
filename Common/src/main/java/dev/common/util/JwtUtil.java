package dev.common.util;

import dev.common.constant.ValueConstant.*;
import dev.common.model.AuthenticatedUser;
import dev.common.model.Permission;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {
    @Value(JWT.SECRET_KEY)
    private String SECRET_KEY;

    // Lấy thông tin user từ jwt
    public AuthenticatedUser getUserFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        List<String> roles = (List<String>) claims.get("roles");
        Set<Permission> permissions = roles.stream().map(role -> Permission.valueOf(role)).collect(Collectors.toSet());
        return AuthenticatedUser.builder()
                .employeeId(UUID.fromString(claims.getSubject()))
                .permissions(permissions)
                .build();
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