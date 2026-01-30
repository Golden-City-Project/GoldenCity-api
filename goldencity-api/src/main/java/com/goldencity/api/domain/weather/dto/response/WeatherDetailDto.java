package com.goldencity.api.domain.weather.dto.response;

import lombok.*;

// 날씨는 실시간 api호출이므로 엔터티가 없고 DTO와 서비스, 컨트롤러만 있음.
// 날씨 상세 정보 (시간별)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WeatherDetailDto {

    // 예보 날짜 (yyyyMMdd)
    private String fcstDate;

    // 예보 시각 (HHmm)
    private String fcstTime;

    // 기온 (℃)
    private String temperature;

    // 하늘 상태 (맑음/구름많음/흐림)
    private String skyStatus;

    // 강수 형태 (없음/비/눈/비+눈)
    private String precipitationType;

    // 강수 확률 (%)
    private String precipitationProbability;

    // 1시간 강수량 (mm)
    private String precipitation;

    // 1시간 적설량 (cm)
    private String snowfall;

    // 습도 (%)
    private String humidity;

    // 풍속 (m/s)
    private String windSpeed;

}