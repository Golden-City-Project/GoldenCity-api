package com.goldencity.api.domain.restaurant.dto.response;

import com.goldencity.api.domain.restaurant.entity.HotplaceType;
import com.goldencity.api.domain.restaurant.entity.MenuType;
import com.goldencity.api.domain.restaurant.entity.RestaurantType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// 식당 상세 응답 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RestaurantDetailResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private RestaurantType restaurantType;
    private MenuType menuType;
    private HotplaceType hotplaceType;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String summary;
    private String content;
    private String homepageUrl;
    private String imageUrl;
    private String keywords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}