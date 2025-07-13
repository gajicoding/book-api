package com.example.book_api.global.config;

import com.example.book_api.domain.user.enums.Role;
import com.example.book_api.global.filter.JwtFilter;
import com.example.book_api.global.util.handler.CustomAccessDeniedHandler;
import com.example.book_api.global.util.handler.CustomAuthenticationEntryPoint;
import com.example.book_api.global.util.jwt.JwtBlacklistService;
import com.example.book_api.global.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // 세션 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 접근 제어
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/auth/**").permitAll()
                        .requestMatchers("/v1/admin/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/v1/books/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/v1/books/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/v1/books/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/**").authenticated()


                        .anyRequest().denyAll()
                )

                // 필터 등록
                .addFilterBefore(new JwtFilter(jwtBlacklistService, jwtUtil), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )

                .build();
    }
}
