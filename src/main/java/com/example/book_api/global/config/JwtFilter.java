package com.example.book_api.global.config;

import com.example.book_api.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String url = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();

        // "/auth" 로 시작하는 url 제외
        if (url.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpServletRequest.getHeader("Authorization");

        // 해더에서 토근 입력하지 않은 경우
        if (bearerJwt == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "토큰이 필요합니다.");
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 토큰입니다.");
            }

            Role role = Role.valueOf(claims.get("role", String.class));

            httpServletRequest.setAttribute("Id", Long.parseLong(claims.getSubject()));
            httpServletRequest.setAttribute("email", claims.get("email"));
            httpServletRequest.setAttribute("name", claims.get("name"));

            AntPathMatcher pathMatcher = new AntPathMatcher();

            if (
                    (pathMatcher.match("/api/v1/books/{id}", url) && "POST".equalsIgnoreCase(method)) ||
                    (pathMatcher.match("/api/v1/books/{id}", url) && "PATCH".equalsIgnoreCase(method)) ||
                    (pathMatcher.match("/api/v1/books/{id}", url) && "DELETE".equalsIgnoreCase(method)) ||
                    (pathMatcher.match("/api/v1/users/{id}", url) && "PATCH".equalsIgnoreCase(method))
            ) {
                if (Role.ADMIN.equals(role)) {
                    httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
                    return;
                }
                chain.doFilter(request,response);
                return;
            }

            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 입니다.");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
