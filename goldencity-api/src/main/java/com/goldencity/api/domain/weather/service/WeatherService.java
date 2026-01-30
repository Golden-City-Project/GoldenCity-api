package com.goldencity.api.domain.weather.service;

import com.goldencity.api.domain.weather.client.WeatherApiClient; // 분리한 클라이언트 임포트
import com.goldencity.api.domain.weather.dto.response.ForecastResponseDto;
import com.goldencity.api.domain.weather.dto.response.WeatherDetailDto;
import com.goldencity.api.domain.weather.dto.response.WeatherResponseDto;
import com.goldencity.api.global.exception.ErrorCode;
import com.goldencity.api.global.exception.ExternalApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor; // 생성자 주입을 위한 어노테이션
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor // weatherApiClient와 objectMapper를 자동으로 주입해줍니다.
public class WeatherService {

    private final WeatherApiClient weatherApiClient; // 서비스는 이제 클라이언트를 통해 데이터를 가져옵니다.
    private final ObjectMapper objectMapper;

    @Value("${api.weather.nx}")
    private int nx;

    @Value("${api.weather.ny}")
    private int ny;

    // 현재 날씨 조회
    public WeatherResponseDto getCurrentWeather() {
        LocalDateTime now = LocalDateTime.now();
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = getBaseTime(now);

        try {
            // [변경점] 직접 callWeatherApi를 호출하지 않고, weatherApiClient에게 시킵니다.
            String response = weatherApiClient.fetchRawWeatherData(baseDate, baseTime, nx, ny, 14);

            if (response == null || response.isBlank()) {
                throw new ExternalApiException(ErrorCode.WEATHER_API_ERROR, "기상청 응답이 비어있습니다.");
            }

            return parseCurrentWeather(response, baseTime);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("[날씨 API 호출 실패] 에러: {}", e.getMessage(), e);
            throw new ExternalApiException(ErrorCode.WEATHER_API_ERROR, e.getMessage());
        }
    }

    // 단기예보 조회
    public ForecastResponseDto getForecast(String targetDate) {
        LocalDateTime now = LocalDateTime.now();
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = getBaseTime(now);

        try {
            // [변경점] 300줄 정도의 큰 데이터를 클라이언트를 통해 가져옵니다.
            String response = weatherApiClient.fetchRawWeatherData(baseDate, baseTime, nx, ny, 300);

            if (response == null || response.isBlank()) {
                throw new ExternalApiException(ErrorCode.WEATHER_API_ERROR, "예보 응답 데이터가 비어있습니다.");
            }

            return parseForecast(response, baseDate, baseTime, targetDate);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("[날씨 예보 API 호출 실패] 에러: {}", e.getMessage(), e);
            throw new ExternalApiException(ErrorCode.WEATHER_API_ERROR, e.getMessage());
        }
    }

    // [변경점] 기존에 있던 private String callWeatherApi(...) 메서드는
    // 이제 WeatherApiClient로 이사 갔으므로 여기서 삭제되었습니다. 코드가 훨씬 간결해졌죠!

    // 현재 날씨 파싱 (단위 포함) - 기존 로직 유지
    private WeatherResponseDto parseCurrentWeather(String response, String baseTime) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            if (!items.isArray() || items.isEmpty()) {
                throw new ExternalApiException(ErrorCode.WEATHER_API_PARSE_ERROR, "아이템이 없습니다.");
            }

            Map<String, String> weatherData = new HashMap<>();
            for (JsonNode item : items) {
                String category = item.path("category").asText("");
                String value = item.path("fcstValue").asText("");
                String fcstTime = item.path("fcstTime").asText("");

                if (weatherData.isEmpty() || fcstTime.equals(weatherData.get("fcstTime"))) {
                    weatherData.putIfAbsent("fcstTime", fcstTime);
                    weatherData.put(category, value);
                }
            }

            return WeatherResponseDto.builder()
                    .currentTemperature(appendUnit(weatherData.get("TMP"), "℃"))
                    .skyStatus(convertSkyCode(weatherData.get("SKY")))
                    .precipitationType(convertPtyCode(weatherData.get("PTY")))
                    .humidity(appendUnit(weatherData.get("REH"), "%"))
                    .windSpeed(appendUnit(weatherData.get("WSD"), "m/s"))
                    .baseTime(baseTime)
                    .build();

        } catch (Exception e) {
            throw new ExternalApiException(ErrorCode.WEATHER_API_PARSE_ERROR);
        }
    }

    // 예보 파싱 (단위 포함) - 기존 로직 유지
    private ForecastResponseDto parseForecast(String response, String baseDate, String baseTime, String targetDate) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            Map<String, Map<String, String>> forecastMap = new HashMap<>();
            for (JsonNode item : items) {
                String fcstDate = item.path("fcstDate").asText("");
                String fcstTime = item.path("fcstTime").asText("");
                if (fcstDate.isEmpty() || (targetDate != null && !fcstDate.equals(targetDate))) continue;

                String key = fcstDate + fcstTime;
                forecastMap.putIfAbsent(key, new HashMap<>());
                forecastMap.get(key).put("fcstDate", fcstDate);
                forecastMap.get(key).put("fcstTime", fcstTime);
                forecastMap.get(key).put(item.path("category").asText(), item.path("fcstValue").asText());
            }

            List<WeatherDetailDto> forecasts = forecastMap.values().stream()
                    .map(data -> WeatherDetailDto.builder()
                            .fcstDate(data.get("fcstDate"))
                            .fcstTime(data.get("fcstTime"))
                            .temperature(appendUnit(data.get("TMP"), "℃"))
                            .skyStatus(convertSkyCode(data.get("SKY")))
                            .precipitationType(convertPtyCode(data.get("PTY")))
                            .precipitationProbability(appendUnit(data.get("POP"), "%"))
                            .precipitation(formatPrecipitation(data.get("PCP")))
                            .snowfall(formatPrecipitation(data.get("SNO")))
                            .humidity(appendUnit(data.get("REH"), "%"))
                            .windSpeed(appendUnit(data.get("WSD"), "m/s"))
                            .build())
                    .sorted((a, b) -> (a.getFcstDate() + a.getFcstTime()).compareTo(b.getFcstDate() + b.getFcstTime()))
                    .toList();

            return ForecastResponseDto.builder()
                    .baseDate(baseDate).baseTime(baseTime).forecasts(forecasts)
                    .lastUpdate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .build();
        } catch (Exception e) {
            throw new ExternalApiException(ErrorCode.WEATHER_API_PARSE_ERROR);
        }
    }

    // [단위 추가] 일반 숫자 데이터 정제 - 기존 로직 유지
    private String appendUnit(String value, String unit) {
        if (value == null || value.isBlank() || value.equalsIgnoreCase("null")) {
            return "정보없음";
        }
        return value.trim() + unit;
    }

    // [강수/적설 정제] - 기존 로직 유지
    private String formatPrecipitation(String value) {
        if (value == null || value.isBlank() || value.contains("없음")) {
            return "없음";
        }
        String trimmed = value.trim();
        if (trimmed.contains("mm") || trimmed.contains("cm")) return trimmed;
        return trimmed + "mm";
    }

    // 시간 계산 로직 - 기존 로직 유지
    private String getBaseTime(LocalDateTime now) {
        int hour = now.getHour();
        if (hour < 2) return "2300";
        if (hour < 5) return "0200";
        if (hour < 8) return "0500";
        if (hour < 11) return "0800";
        if (hour < 14) return "1100";
        if (hour < 17) return "1400";
        if (hour < 20) return "1700";
        if (hour < 23) return "2000";
        return "2300";
    }

    // 코드 변환 로직 - 기존 로직 유지
    private String convertSkyCode(String code) {
        if (code == null || code.isBlank()) return "정보없음";
        return switch (code) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "정보없음";
        };
    }

    private String convertPtyCode(String code) {
        if (code == null || code.isBlank()) return "정보없음";
        return switch (code) {
            case "0" -> "없음";
            case "1" -> "비";
            case "2" -> "비/눈";
            case "3" -> "눈";
            case "4" -> "소나기";
            default -> "정보없음";
        };
    }
}