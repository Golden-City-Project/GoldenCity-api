package com.goldencity.api.domain.restaurant.dto.response;

import com.goldencity.api.domain.restaurant.entity.HotplaceType;
import com.goldencity.api.domain.restaurant.entity.MenuType;
import com.goldencity.api.domain.restaurant.entity.RestaurantType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

// 식당 응답 DTO (목록 조회용)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RestaurantResponseDto implements Serializable {

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
    private String imageUrl;
}