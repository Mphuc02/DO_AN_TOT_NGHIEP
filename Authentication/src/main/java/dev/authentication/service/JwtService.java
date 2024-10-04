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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    @Value(JWT.SECRET_KEY)
    private String SECRET_KEY;

    @Value(JWT.ACCESS_TOKEN_EXPIRATION)
    public int ACCESS_TOKEN_EXPIRATION;

    @Value(JWT.REFRESH_TOKEN_EXPIRATION)
    public int REFRESH_TOKEN_EXPIRATION;

    public String generateAccessToken(Account account, List<Permission> permissions) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);

        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .claim("userId", account.getId().toString())
                .claim("roles", permissions)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(Account account, List<Permission> permissions){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .claim("userId", account.getId().toString())
                .claim("roles", permissions)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}