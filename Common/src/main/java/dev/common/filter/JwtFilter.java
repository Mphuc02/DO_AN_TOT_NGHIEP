package dev.common.filter;

import dev.common.constant.ApiConstant;
import dev.common.constant.JwtConstant;
import dev.common.model.AuthenticatedUser;
import dev.common.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

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
        String requestPath = request.getServletPath();
        if(requestPath.startsWith(ApiConstant.AUTHENTICATION_URL.URL)){
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = this.getJwtFromHeader(request);
        if(ObjectUtils.isEmpty(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        response.sendError(401, "oke");
        return;

//        jwtUtil.validateToken(jwt);
//        AuthenticatedUser user = jwtUtil.getUserFromJWT(jwt);
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
//                null,
//                user.getPermissions()
//                        .stream()
//                        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
//                        .collect(Collectors.toList()));
//
//        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//        filterChain.doFilter(request, response);
    }
}