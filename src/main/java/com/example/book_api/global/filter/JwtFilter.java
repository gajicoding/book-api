package com.example.book_api.global.filter;

import com.example.book_api.domain.auth.dto.AuthUser;
import com.example.book_api.global.exception.TokenNotFoundException;
import com.example.book_api.global.exception.enums.ServletResponseError;
import com.example.book_api.global.util.jwt.JwtBlacklistService;
import com.example.book_api.global.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null || !bearerJwt.matches("^Bearer\\s+[A-Za-z0-9-_.]+$")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtil.extractToken(bearerJwt);

        try {
            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                response.sendError(ServletResponseError.INVALID_JWT.getHttpStatus(), ServletResponseError.INVALID_JWT.getMessage());
                return;
            }

            // 블랙리스트 검증
            if (jwtBlacklistService.isBlacklisted(jwt)) {
                response.sendError(ServletResponseError.EXPIRED_JWT_TOKEN.getHttpStatus(), ServletResponseError.EXPIRED_JWT_TOKEN.getMessage());
                return;
            }

            AuthUser authUser = new AuthUser(
                    jwtUtil.getId(claims),
                    jwtUtil.getEmail(claims),
                    jwtUtil.getName(claims),
                    jwtUtil.getRole(claims)
            );


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    authUser, null, List.of(new SimpleGrantedAuthority("ROLE_"+ jwtUtil.getRole(claims).name()))
            );

            // 인증 정보 등록
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);

        } catch (SecurityException | MalformedJwtException | TokenNotFoundException e) {
            response.sendError(ServletResponseError.INVALID_JWT_SIGNATURE.getHttpStatus(), ServletResponseError.INVALID_JWT_SIGNATURE.getMessage());
        } catch (ExpiredJwtException e) {
            response.sendError(ServletResponseError.EXPIRED_JWT_TOKEN.getHttpStatus(), ServletResponseError.EXPIRED_JWT_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            response.sendError(ServletResponseError.UNSUPPORTED_JWT.getHttpStatus(), ServletResponseError.UNSUPPORTED_JWT.getMessage());
        } catch (RedisConnectionFailureException e) {
            response.sendError(ServletResponseError.REDIS_CONNECTION_FAILED.getHttpStatus(), ServletResponseError.REDIS_CONNECTION_FAILED.getMessage());
        } catch (DataAccessException e) {
            response.sendError(ServletResponseError.DATA_ACCESS_FAILED.getHttpStatus(), ServletResponseError.DATA_ACCESS_FAILED.getMessage());
        } catch (Exception e) {
            response.sendError(ServletResponseError.INTERNAL_SERVER_ERROR.getHttpStatus(), ServletResponseError.INTERNAL_SERVER_ERROR.getMessage());
        }
    }
}
