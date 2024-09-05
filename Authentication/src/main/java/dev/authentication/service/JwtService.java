package dev.authentication.service;

import dev.common.constant.ValueConstant.*;
import dev.authentication.entity.Account;
import dev.common.model.Permission;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    @Value(JWT.SECRET_KEY)
    private String SECRET_KEY;

    public String generateToken(Account account, long expiration, List<Permission> permissions) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(account.getId().toString())
                .claim("roles", permissions)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}