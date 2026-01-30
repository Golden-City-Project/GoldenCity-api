package com.goldencity.api.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보호 비활성화 (API 서버니까 일단 꺼두는 게 편해)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 우리 관광지, 맛집 API 주소들은 모두 허용!
                        .requestMatchers("/api/v1/**").permitAll()
                        // 나머지는 그래도 혹시 모르니 인증받게 두자
                        .anyRequest().authenticated()
                )

                // 3. 기본 로그인 폼이랑 Basic Auth 인증창 안 뜨게 설정
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}