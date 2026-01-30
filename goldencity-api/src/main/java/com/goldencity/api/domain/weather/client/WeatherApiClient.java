package com.goldencity.api.domain.weather.client;

import com.goldencity.api.global.exception.ErrorCode;
import com.goldencity.api.global.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherApiClient {

    private final WebClient webClient;

    @Value("${api.weather.service-key}")
    private String serviceKey;

    @Value("${api.weather.url}")
    private String apiUrl;

    /**
     * 기상청 외부 API를 실제로 호출하는 메서드
     */
    public String fetchRawWeatherData(String baseDate, String baseTime, int nx, int ny, int numOfRows) {
        // 1. URI 조립 (ServiceKey 인코딩 문제를 피하기 위해 직접 포맷팅)
        String url = String.format("%s?serviceKey=%s&pageNo=%d&numOfRows=%d&dataType=JSON&base_date=%s&base_time=%s&nx=%d&ny=%d",
                apiUrl, serviceKey, 1, numOfRows, baseDate, baseTime, nx, ny);

        try {
            java.net.URI uri = new java.net.URI(url);

            return webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(res -> log.info("[기상청 API 호출 성공]"))
                    // [안정성] 네트워크 일시적 오류 대비 3번 재시도
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                    .block();
        } catch (Exception e) {
            log.error("[기상청 API 호출 실패] URI: {}, 에러: {}", url, e.getMessage());
            // [에러 처리] 외부 API 호출 실패 시 커스텀 예외 발생
            throw new ExternalApiException(ErrorCode.WEATHER_API_ERROR, "기상청 시스템 연결 실패");
        }
    }
}