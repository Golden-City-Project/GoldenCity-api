package com.goldencity.api.domain.accommodation.controller;

import com.goldencity.api.domain.accommodation.dto.response.AccommodationDetailResponseDto;
import com.goldencity.api.domain.accommodation.dto.response.AccommodationResponseDto;
import com.goldencity.api.domain.accommodation.entity.AccommodationType;
import com.goldencity.api.domain.accommodation.service.AccommodationService;
import com.goldencity.api.global.common.ApiResponse;
import com.goldencity.api.global.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// 숙박 REST API 컨트롤러
@RestController
@RequestMapping("/api/v1/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    // 전체 숙박업소 조회
    @GetMapping
    public ApiResponse<PageResponse<AccommodationResponseDto>> getAllAccommodations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AccommodationResponseDto> accommodations = accommodationService.getAllAccommodations(page, size);
        String warning = checkDataFreshness(accommodationService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(accommodations), warning);
    }

    // 타입별 조회
    @GetMapping("/type/{type}")
    public ApiResponse<PageResponse<AccommodationResponseDto>> getAccommodationsByType(
            @PathVariable AccommodationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AccommodationResponseDto> accommodations = accommodationService.getAccommodationsByType(type, page, size);
        String warning = checkDataFreshness(accommodationService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(accommodations), warning);
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ApiResponse<AccommodationDetailResponseDto> getAccommodationById(@PathVariable Long id) {
        AccommodationDetailResponseDto accommodation = accommodationService.getAccommodationById(id);
        return ApiResponse.success(accommodation);
    }

    // 검색
    @GetMapping("/search")
    public ApiResponse<PageResponse<AccommodationResponseDto>> searchAccommodations(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AccommodationResponseDto> accommodations = accommodationService.searchAccommodations(keyword, page, size);
        String warning = checkDataFreshness(accommodationService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(accommodations), warning);
    }

    // 데이터 신선도 체크 (168시간 = 7일 기준)
    private String checkDataFreshness(LocalDateTime latestUpdate) {
        if (latestUpdate == null) {
            return "데이터 업데이트 정보를 확인할 수 없습니다";
        }

        long hoursSinceUpdate = ChronoUnit.HOURS.between(latestUpdate, LocalDateTime.now());

        if (hoursSinceUpdate > 168) {
            return String.format("현재 데이터는 %d시간 전에 수집되었습니다. 최신 정보가 아닐 수 있습니다", hoursSinceUpdate);
        }

        return null;
    }
}