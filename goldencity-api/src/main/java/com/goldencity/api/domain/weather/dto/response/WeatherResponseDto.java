package com.goldencity.api.domain.weather.dto.response;

import lombok.*;

// 현재 날씨 응답 DTO (화면 상단 표시용)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WeatherResponseDto {

    // 현재 기온 (℃)
    private String currentTemperature;

    // 하늘 상태 (맑음/구름많음/흐림)
    private String skyStatus;

    // 강수 형태 (없음/비/눈/비+눈)
    private String precipitationType;

    // 습도 (%)
    private String humidity;

    // 풍속 (m/s)
    private String windSpeed;

    // 데이터 기준 시각
    private String baseTime;
}