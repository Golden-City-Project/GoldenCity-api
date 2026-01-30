package com.goldencity.api.domain.restaurant.entity;

// 식당 타입 구분 (메뉴별 vs 핫플레이스)
public enum RestaurantType {
    MENU("메뉴별 음식점"),
    HOTPLACE("먹거리 핫플레이스");

    private final String description;

    RestaurantType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}