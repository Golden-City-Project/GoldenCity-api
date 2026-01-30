package com.goldencity.api.domain.weather.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 단기예보 응답 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ForecastResponseDto {

    // 기준 날짜
    private String baseDate;

    // 기준 시각
    private String baseTime;

    // 예보 데이터 리스트 (최대 3일치)
    // [널 처리 추가] 리스트 자체를 초기화하여 NPE를 방지합니다.
    // Builder 사용 시에도 기본값으로 적용되도록 @Builder.Default를 씁니다.
    @Builder.Default
    private List<WeatherDetailDto> forecasts = new ArrayList<>();

    // 데이터 업데이트 시각
    private String lastUpdate;
}