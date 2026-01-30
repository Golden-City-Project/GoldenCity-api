package com.goldencity.api.domain.tourspot.controller;

import com.goldencity.api.domain.tourspot.dto.response.TourSpotDetailResponseDto;
import com.goldencity.api.domain.tourspot.dto.response.TourSpotResponseDto;
import com.goldencity.api.domain.tourspot.entity.AreaCode;
import com.goldencity.api.domain.tourspot.service.TourSpotService;
import com.goldencity.api.global.common.ApiResponse;
import com.goldencity.api.global.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// 관광지 REST API 컨트롤러
@RestController
@RequestMapping("/api/v1/tourspots")
public class TourSpotController {

    private final TourSpotService tourSpotService;

    public TourSpotController(TourSpotService tourSpotService) {
        this.tourSpotService = tourSpotService;
    }

    // 전체 관광지 조회
    @GetMapping
    public ApiResponse<PageResponse<TourSpotResponseDto>> getAllTourSpots(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<TourSpotResponseDto> tourSpotPage = tourSpotService.getAllTourSpots(page, size);
        String warning = checkDataFreshness(tourSpotService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(tourSpotPage), warning);
    }

    // 권역별 조회
    @GetMapping("/area/{areaCode}")
    public ApiResponse<PageResponse<TourSpotResponseDto>> getTourSpotsByArea(
            @PathVariable AreaCode areaCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<TourSpotResponseDto> tourSpotPage = tourSpotService.getTourSpotsByArea(areaCode, page, size);
        String warning = checkDataFreshness(tourSpotService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(tourSpotPage), warning);
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ApiResponse<TourSpotDetailResponseDto> getTourSpotById(@PathVariable Long id) {
        TourSpotDetailResponseDto tourSpot = tourSpotService.getTourSpotById(id);
        return ApiResponse.success(tourSpot);
    }

    // 이름 검색
    @GetMapping("/search")
    public ApiResponse<PageResponse<TourSpotResponseDto>> searchTourSpots(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<TourSpotResponseDto> tourSpotPage = tourSpotService.searchTourSpotsByName(keyword, page, size);
        String warning = checkDataFreshness(tourSpotService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(tourSpotPage), warning);
    }

    // 데이터 신선도 체크 (168시간 = 7일 기준으로 변경)
    private String checkDataFreshness(LocalDateTime latestUpdate) {
        if (latestUpdate == null) {
            return "데이터 업데이트 정보를 확인할 수 없습니다";
        }

        long hoursSinceUpdate = ChronoUnit.HOURS.between(latestUpdate, LocalDateTime.now());

        if (hoursSinceUpdate > 168) {  // 7일 = 168시간
            return String.format("현재 데이터는 %d시간 전에 수집되었습니다. 최신 정보가 아닐 수 있습니다", hoursSinceUpdate);
        }

        return null;
    }
}