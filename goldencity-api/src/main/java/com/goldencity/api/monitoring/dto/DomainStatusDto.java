package com.goldencity.api.monitoring.dto;

import lombok.*;

import java.time.LocalDateTime;

// 도메인별 상태 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DomainStatusDto {

    private String domainName;
    private LocalDateTime lastUpdateTime;
    private long totalRecords;
    private long hoursSinceUpdate;
    private String status;
}