package com.goldencity.api.global.config;

import org.springframework.context.annotation.Configuration;

// Vault 설정
// application-vault.yml에서 자동 구성되므로
// 추가 Bean 설정 불필요
//
// Vault 사용 방법:
// 1. Vault 서버 실행
// 2. 환경변수 설정: VAULT_TOKEN, VAULT_HOST
// 3. application-vault.yml의 경로에 secret 저장
//
// Vault에 저장할 정보:
// - DB_USERNAME
// - DB_PASSWORD
// - REDIS_PASSWORD
// - 외부 API 키들
@Configuration
public class VaultConfig {
    // Spring Cloud Vault가 자동으로 설정 처리
}