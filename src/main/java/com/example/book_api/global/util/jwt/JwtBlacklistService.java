package com.example.book_api.global.util.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {
    private static final String BLACKLIST_PREFIX = "BL:";

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;


    // 토큰을 블랙리스트에 저장
    public void addBlacklist(String token) {
        long expirationMillis = jwtUtil.getRemainingExpiration(token);

        redisTemplate.opsForValue()
                .set(getKey(token), "true", expirationMillis, TimeUnit.MILLISECONDS);
    }

    // 요청 시 토큰이 블랙리스트인지 확인
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getKey(token)));
    }

    private String getKey(String token) {
        return BLACKLIST_PREFIX + token;
    }
}
