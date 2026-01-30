package com.goldencity.api.domain.weather.controller;

import com.goldencity.api.domain.weather.dto.response.ForecastResponseDto;
import com.goldencity.api.domain.weather.dto.response.WeatherResponseDto;
import com.goldencity.api.domain.weather.service.WeatherService;
import com.goldencity.api.global.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

// 날씨 REST API 컨트롤러
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // 현재 날씨 조회 (화면 상단 표시용)
    @GetMapping("/current")
    public ApiResponse<WeatherResponseDto> getCurrentWeather() {
        WeatherResponseDto weather = weatherService.getCurrentWeather();
        return ApiResponse.success(weather);
    }

    // 단기예보 조회 (날씨 탭 - 최대 3일)
    @GetMapping("/forecast")
    public ApiResponse<ForecastResponseDto> getForecast(
            @RequestParam(required = false) String targetDate
    ) {
        ForecastResponseDto forecast = weatherService.getForecast(targetDate);
        return ApiResponse.success(forecast);
    }
}