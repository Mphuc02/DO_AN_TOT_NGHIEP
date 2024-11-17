package dev.common.filter;

import dev.common.constant.JwtConstant;
import dev.common.constant.RedisKeyConstrant;
import dev.common.model.AuthenticatedUser;
import dev.common.model.TokenType;
import dev.common.service.RedisService;
import dev.common.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    private String getJwtFromHeader(HttpServletRequest request){
        final String authHeader = request.getHeader(JwtConstant.AUTH_HEADER);

        if (authHeader == null || !authHeader.startsWith(JwtConstant.BEARER)) {
            return null;
        }

        String jwt = authHeader.substring(JwtConstant.INDEX_OF_JWT);
        return jwt.equals("null") ? null : jwt;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String jwt = this.getJwtFromHeader(request);
        if(ObjectUtils.isEmpty(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID tokenId = jwtUtil.getTokenId(jwt);
            String findInvalidToken = (String) redisService.getValue(RedisKeyConstrant.INVALID_TOKEN_KEY(tokenId),
                    String.class);
            if(!ObjectUtils.isEmpty(findInvalidToken))
                throw new JwtException("Jwt has been revoked");

            AuthenticatedUser user = jwtUtil.getUserFromJwt(jwt, TokenType.ACCESS_TOKEN);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                    null,
                    user.getRoles().stream().map(permission -> new SimpleGrantedAuthority(permission.toString())).toList());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            log.error("Exception when extra User from Jwt", ex);
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(ex.getMessage());
        }
    }
}