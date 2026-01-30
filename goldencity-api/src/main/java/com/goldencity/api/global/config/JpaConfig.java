package com.goldencity.api.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// JPA Auditing 활성화
// BaseEntity의 createdAt, updatedAt 자동 관리
@Configuration
@EnableJpaAuditing // BaseEntity의 생성일시, 수정일시 자동 관리
public class JpaConfig {
}