package com.goldencity.api.domain.restaurant.service;

import com.goldencity.api.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.goldencity.api.domain.restaurant.dto.response.RestaurantResponseDto;
import com.goldencity.api.domain.restaurant.entity.HotplaceType;
import com.goldencity.api.domain.restaurant.entity.MenuType;
import com.goldencity.api.domain.restaurant.entity.Restaurant;
import com.goldencity.api.domain.restaurant.entity.RestaurantType;
import com.goldencity.api.domain.restaurant.repository.RestaurantRepository;
import com.goldencity.api.global.exception.DataNotFoundException;
import com.goldencity.api.global.exception.ErrorCode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// 식당 비즈니스 로직
@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // 전체 식당 조회 (페이징)
    @Cacheable(
            value = "restaurants",
            key = "'page_' + #page + '_size_' + #size",
            // 널처리 추가
            unless = "#result == null || #result.isEmpty()"
    )
    public Page<RestaurantResponseDto> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);
        return restaurants.map(this::convertToResponseDto);
    }

    // 메뉴별 음식점 조회 (한식/중식/일식/양식)
    @Cacheable(
            value = "restaurants-menu",
            key = "#menuType + '_page_' + #page + '_size_' + #size",
            // 널처리 추가
            unless = "#result == null || #result.isEmpty()"
    )
    public Page<RestaurantResponseDto> getRestaurantsByMenuType(MenuType menuType, int page, int size) {
        if (menuType == null) return Page.empty();

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Restaurant> restaurants = restaurantRepository.findByRestaurantTypeAndMenuType(
                RestaurantType.MENU, menuType, pageable
        );
        return restaurants.map(this::convertToResponseDto);
    }

    // 먹거리 핫플레이스 조회
    @Cacheable(
            value = "restaurants-hotplace",
            key = "#hotplaceType + '_page_' + #page + '_size_' + #size",
            // 널처리 추가
            unless = "#result == null || #result.isEmpty()"
    )
    public Page<RestaurantResponseDto> getRestaurantsByHotplaceType(HotplaceType hotplaceType, int page, int size) {
        if (hotplaceType == null) return Page.empty();

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Restaurant> restaurants = restaurantRepository.findByRestaurantTypeAndHotplaceType(
                RestaurantType.HOTPLACE, hotplaceType, pageable
        );
        return restaurants.map(this::convertToResponseDto);
    }

    // 상세 조회
    @Cacheable(value = "restaurant-detail", key = "#id")
    public RestaurantDetailResponseDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));
        return convertToDetailResponseDto(restaurant);
    }

    // 검색 (이름 + 키워드)
    @Cacheable(
            value = "restaurants-search",
            key = "#keyword + '_page_' + #page + '_size_' + #size",
            unless = "#result == null || #result.isEmpty()"
    )
    public Page<RestaurantResponseDto> searchRestaurants(String keyword, int page, int size) {
        if (keyword == null || keyword.trim().isEmpty()) return Page.empty();

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Restaurant> restaurants = restaurantRepository.searchByKeyword(keyword, pageable);
        return restaurants.map(this::convertToResponseDto);
    }

    // 최신 업데이트 시간 조회
    public LocalDateTime getLatestUpdateTime() {
        return restaurantRepository.findLatestUpdatedAt().orElse(null);
    }

    // Entity → ResponseDto 변환
    private RestaurantResponseDto convertToResponseDto(Restaurant restaurant) {
        if (restaurant == null) return null;

        return RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .name(handleNullString(restaurant.getName()))
                .address(handleNullString(restaurant.getAddress()))
                .phoneNumber(handleNullString(restaurant.getPhoneNumber()))
                .restaurantType(restaurant.getRestaurantType())
                .menuType(restaurant.getMenuType())
                .hotplaceType(restaurant.getHotplaceType())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .imageUrl(handleNullString(restaurant.getImageUrl()))
                .build();
    }

    // Entity → DetailResponseDto 변환
    private RestaurantDetailResponseDto convertToDetailResponseDto(Restaurant restaurant) {
        if (restaurant == null) return null;

        return RestaurantDetailResponseDto.builder()
                .id(restaurant.getId())
                .name(handleNullString(restaurant.getName()))
                .address(handleNullString(restaurant.getAddress()))
                .phoneNumber(handleNullString(restaurant.getPhoneNumber()))
                .restaurantType(restaurant.getRestaurantType())
                .menuType(restaurant.getMenuType())
                .hotplaceType(restaurant.getHotplaceType())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .summary(handleNullString(restaurant.getSummary()))
                .content(handleNullString(restaurant.getContent()))
                .homepageUrl(handleNullString(restaurant.getHomepageUrl()))
                .imageUrl(handleNullString(restaurant.getImageUrl()))
                .keywords(handleNullString(restaurant.getKeywords()))
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .build();
    }

    // [널 처리 추가] 문자열 널 방어용 공통 메서드
    private String handleNullString(String value) {
        return (value == null || value.trim().isEmpty()) ? "" : value;
    }
}