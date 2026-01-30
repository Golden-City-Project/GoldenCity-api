package com.goldencity.api.domain.restaurant.entity;

import com.goldencity.api.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// 식당 정보 Entity
// Lombok 사용으로 코드 간결화
@Entity
@Table(
        name = "restaurants",
        indexes = {
                @Index(name = "idx_restaurant_name", columnList = "name"),
                @Index(name = "idx_restaurant_type", columnList = "restaurant_type"),
                @Index(name = "idx_menu_type", columnList = "menu_type"),
                @Index(name = "idx_hotplace_type", columnList = "hotplace_type")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Restaurant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 식당 이름
    @Column(nullable = false, length = 200)
    private String name;

    // 주소
    @Column(length = 500)
    private String address;

    // 전화번호
    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    // 식당 타입 (메뉴별 vs 핫플레이스)
    @Enumerated(EnumType.STRING)
    @Column(name = "restaurant_type", length = 20, nullable = false)
    private RestaurantType restaurantType;

    // 메뉴 타입 (한식/중식/일식/양식) - 메뉴별 음식점인 경우만
    @Enumerated(EnumType.STRING)
    @Column(name = "menu_type", length = 20)
    private MenuType menuType;

    // 핫플레이스 타입 - 핫플레이스인 경우만
    @Enumerated(EnumType.STRING)
    @Column(name = "hotplace_type", length = 50)
    private HotplaceType hotplaceType;

    // 위도
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    // 경도
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    // 요약 정보 (영업시간, 대표메뉴 등)
    @Column(columnDefinition = "TEXT")
    private String summary;

    // 상세 설명
    @Column(columnDefinition = "TEXT")
    private String content;

    // 홈페이지 URL
    @Column(name = "homepage_url", length = 500)
    private String homepageUrl;

    // 이미지 URL
    @Column(name = "image_url", length = 500)
    private String imageUrl;


    // 키워드 (검색용)
    @Column(length = 500)
    private String keywords;
}