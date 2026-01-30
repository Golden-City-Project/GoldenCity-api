package com.goldencity.api.monitoring.controller;

import com.goldencity.api.domain.accommodation.repository.AccommodationRepository;
import com.goldencity.api.domain.restaurant.repository.RestaurantRepository;
import com.goldencity.api.domain.tourspot.repository.TourSpotRepository;
import com.goldencity.api.global.common.ApiResponse;
import com.goldencity.api.monitoring.dto.BatchStatusDto;
import com.goldencity.api.monitoring.dto.DomainStatusDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// 배치 및 데이터 상태 모니터링 API
@RestController
@RequestMapping("/api/v1/monitoring")
public class MonitoringController {

    private final TourSpotRepository tourSpotRepository;
    private final RestaurantRepository restaurantRepository;
    private final AccommodationRepository accommodationRepository;

    public MonitoringController(
            TourSpotRepository tourSpotRepository,
            RestaurantRepository restaurantRepository,
            AccommodationRepository accommodationRepository
    ) {
        this.tourSpotRepository = tourSpotRepository;
        this.restaurantRepository = restaurantRepository;
        this.accommodationRepository = accommodationRepository;
    }

    // 전체 배치 상태 조회
    @GetMapping("/batch-status")
    public ApiResponse<BatchStatusDto> getBatchStatus() {
        // 가장 오래된 도메인 기준으로 상태 반환
        LocalDateTime oldestUpdate = null;
        long totalRecords = 0;

        LocalDateTime tourSpotUpdate = tourSpotRepository.findLatestUpdatedAt().orElse(null);
        LocalDateTime restaurantUpdate = restaurantRepository.findLatestUpdatedAt().orElse(null);
        LocalDateTime accommodationUpdate = accommodationRepository.findLatestUpdatedAt().orElse(null);

        // 가장 오래된 업데이트 시간 찾기
        if (tourSpotUpdate != null) {
            oldestUpdate = tourSpotUpdate;
        }
        if (restaurantUpdate != null && (oldestUpdate == null || restaurantUpdate.isBefore(oldestUpdate))) {
            oldestUpdate = restaurantUpdate;
        }
        if (accommodationUpdate != null && (oldestUpdate == null || accommodationUpdate.isBefore(oldestUpdate))) {
            oldestUpdate = accommodationUpdate;
        }

        // 전체 레코드 수 합산
        totalRecords += tourSpotRepository.countAll();
        totalRecords += restaurantRepository.countAll();
        totalRecords += accommodationRepository.countAll();

        if (oldestUpdate == null) {
            return ApiResponse.error("배치 실행 기록이 없습니다");
        }

        long hoursSinceUpdate = ChronoUnit.HOURS.between(oldestUpdate, LocalDateTime.now());
        BatchStatusDto status = BatchStatusDto.of(oldestUpdate, totalRecords, hoursSinceUpdate);

        return ApiResponse.success(status);
    }

    // 도메인별 상태 조회
    @GetMapping("/domain-status")
    public ApiResponse<List<DomainStatusDto>> getDomainStatus() {
        List<DomainStatusDto> domainStatuses = new ArrayList<>();

        // 관광지
        domainStatuses.add(createDomainStatus(
                "관광지",
                tourSpotRepository.findLatestUpdatedAt().orElse(null),
                tourSpotRepository.countAll()
        ));

        // 식당
        domainStatuses.add(createDomainStatus(
                "식당",
                restaurantRepository.findLatestUpdatedAt().orElse(null),
                restaurantRepository.countAll()
        ));

        // 숙박
        domainStatuses.add(createDomainStatus(
                "숙박",
                accommodationRepository.findLatestUpdatedAt().orElse(null),
                accommodationRepository.countAll()
        ));


        return ApiResponse.success(domainStatuses);
    }

    // 도메인 상태 DTO 생성
    private DomainStatusDto createDomainStatus(String domainName, LocalDateTime lastUpdate, long totalRecords) {
        if (lastUpdate == null) {
            return DomainStatusDto.builder()
                    .domainName(domainName)
                    .lastUpdateTime(null)
                    .totalRecords(totalRecords)
                    .hoursSinceUpdate(0)
                    .status("NO_DATA")
                    .build();
        }

        long hoursSinceUpdate = ChronoUnit.HOURS.between(lastUpdate, LocalDateTime.now());
        String status;

        if (hoursSinceUpdate <= 24) {
            status = "FRESH";
        } else if (hoursSinceUpdate <= 168) {
            status = "WARNING";
        } else {
            status = "STALE";
        }

        return DomainStatusDto.builder()
                .domainName(domainName)
                .lastUpdateTime(lastUpdate)
                .totalRecords(totalRecords)
                .hoursSinceUpdate(hoursSinceUpdate)
                .status(status)
                .build();
    }
}