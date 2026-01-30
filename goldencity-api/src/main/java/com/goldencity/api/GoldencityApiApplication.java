package com.goldencity.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// API 서버 메인 애플리케이션
// Redis 캐싱 활성화
@SpringBootApplication
@EnableCaching  // Redis 캐싱 활성화
public class GoldencityApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldencityApiApplication.class, args);

	}
}