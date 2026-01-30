package com.goldencity.api.domain.restaurant.repository;

import com.goldencity.api.domain.restaurant.entity.HotplaceType;
import com.goldencity.api.domain.restaurant.entity.MenuType;
import com.goldencity.api.domain.restaurant.entity.Restaurant;
import com.goldencity.api.domain.restaurant.entity.RestaurantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// Restaurant Entity Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // 식당 타입별 조회
    Page<Restaurant> findByRestaurantType(RestaurantType restaurantType, Pageable pageable);

    // 메뉴 타입별 조회 (한식/중식/일식/양식)
    Page<Restaurant> findByRestaurantTypeAndMenuType(RestaurantType restaurantType, MenuType menuType, Pageable pageable);

    // 핫플레이스 타입별 조회
    Page<Restaurant> findByRestaurantTypeAndHotplaceType(RestaurantType restaurantType, HotplaceType hotplaceType, Pageable pageable);

    // 이름으로 검색
    Page<Restaurant> findByNameContaining(String keyword, Pageable pageable);

    // 키워드로 검색 (이름 + 키워드 필드)
    @Query("SELECT r FROM Restaurant r WHERE r.name LIKE %:keyword% OR r.keywords LIKE %:keyword%")
    Page<Restaurant> searchByKeyword(String keyword, Pageable pageable);

    // 최신 업데이트 시간 조회
    @Query("SELECT MAX(r.updatedAt) FROM Restaurant r")
    Optional<java.time.LocalDateTime> findLatestUpdatedAt();

    // 전체 개수 조회
    @Query("SELECT COUNT(r) FROM Restaurant r")
    long countAll();
}