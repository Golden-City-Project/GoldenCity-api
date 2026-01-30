package com.goldencity.api.domain.restaurant.controller;

import com.goldencity.api.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.goldencity.api.domain.restaurant.dto.response.RestaurantResponseDto;
import com.goldencity.api.domain.restaurant.entity.HotplaceType;
import com.goldencity.api.domain.restaurant.entity.MenuType;
import com.goldencity.api.domain.restaurant.service.RestaurantService;
import com.goldencity.api.global.common.ApiResponse;
import com.goldencity.api.global.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// 식당 REST API 컨트롤러
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // 전체 식당 조회
    @GetMapping
    public ApiResponse<PageResponse<RestaurantResponseDto>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestaurantResponseDto> restaurants = restaurantService.getAllRestaurants(page, size);
        String warning = checkDataFreshness(restaurantService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(restaurants), warning);
    }

    // 메뉴별 음식점 조회 (한식/중식/일식/양식)
    @GetMapping("/menu/{menuType}")
    public ApiResponse<PageResponse<RestaurantResponseDto>> getRestaurantsByMenuType(
            @PathVariable MenuType menuType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestaurantResponseDto> restaurants = restaurantService.getRestaurantsByMenuType(menuType, page, size);
        String warning = checkDataFreshness(restaurantService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(restaurants), warning);
    }

    // 먹거리 핫플레이스 조회
    @GetMapping("/hotplace/{hotplaceType}")
    public ApiResponse<PageResponse<RestaurantResponseDto>> getRestaurantsByHotplaceType(
            @PathVariable HotplaceType hotplaceType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestaurantResponseDto> restaurants = restaurantService.getRestaurantsByHotplaceType(hotplaceType, page, size);
        String warning = checkDataFreshness(restaurantService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(restaurants), warning);
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ApiResponse<RestaurantDetailResponseDto> getRestaurantById(@PathVariable Long id) {
        RestaurantDetailResponseDto restaurant = restaurantService.getRestaurantById(id);
        return ApiResponse.success(restaurant);
    }

    // 검색 (이름 + 키워드)
    @GetMapping("/search")
    public ApiResponse<PageResponse<RestaurantResponseDto>> searchRestaurants(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestaurantResponseDto> restaurants = restaurantService.searchRestaurants(keyword, page, size);
        String warning = checkDataFreshness(restaurantService.getLatestUpdateTime());
        return ApiResponse.success(PageResponse.of(restaurants), warning);
    }

    // 데이터 신선도 체크 (168시간 = 7일 기준)
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