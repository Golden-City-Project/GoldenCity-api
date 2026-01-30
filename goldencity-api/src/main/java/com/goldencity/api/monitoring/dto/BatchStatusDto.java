package com.goldencity.api.monitoring.dto;

import lombok.*;

import java.time.LocalDateTime;

// 배치 상태 응답 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BatchStatusDto {

    private LocalDateTime lastUpdateTime;
    private long totalRecords;
    private long hoursSinceUpdate;
    private String status;  // FRESH, WARNING, STALE

    public static BatchStatusDto of(LocalDateTime lastUpdateTime, long totalRecords, long hoursSinceUpdate) {
        String status;
        if (hoursSinceUpdate <= 24) {
            status = "FRESH";
        } else if (hoursSinceUpdate <= 168) {  // 7일 이내
            status = "WARNING";
        } else {
            status = "STALE";
        }

        return BatchStatusDto.builder()
                .lastUpdateTime(lastUpdateTime)
                .totalRecords(totalRecords)
                .hoursSinceUpdate(hoursSinceUpdate)
                .status(status)
                .build();
    }
}