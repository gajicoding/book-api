package com.example.book_api.global.util.jwt;

import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.enums.Role;
import com.example.book_api.global.exception.TokenNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(User user) {
        Date date = new Date();

        return Jwts.builder()
                    .setSubject(String.valueOf(user.getId()))
                    .claim("email", user.getEmail())
                    .claim("name", user.getName())
                    .claim("role", user.getRole())
                    .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                    .setIssuedAt(date)
                    .signWith(key, signatureAlgorithm)
                    .compact();
    }

    public String extractToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length()); // "Bearer " 제거 후 반환
        }

        throw new TokenNotFoundException("토큰을 찾을 수 없습니다.");
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getRemainingExpiration(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    public Long getId(Claims claims) {
        return Long.parseLong(claims.getSubject());
    }

    public String getName(Claims claims) {
        return claims.get("name", String.class);
    }

    public String getEmail(Claims claims) {
        return claims.get("email", String.class);
    }

    public Role getRole(Claims claims) {
        return Role.of(claims.get("role", String.class));
    }
}
