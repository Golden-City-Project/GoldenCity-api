package com.goldencity.api.domain.restaurant.entity;

// 먹거리 핫플레이스 타입
public enum HotplaceType {
    HWANGRIDANGIL("황리단길"),
    SUPMEORIFOODCOMPLEX("숲머리음식단지"),
    DESSERT("디저트"),
    SCENIC_CAFE("전망좋은카페");

    private final String description;

    HotplaceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // API 응답의 AREA_NAME을 Enum으로 변환
    public static HotplaceType fromAreaName(String areaName) {
        if (areaName == null || areaName.trim().isEmpty()) {
            return null;
        }

        if (areaName.contains("황리단길")) {
            return HWANGRIDANGIL;
        } else if (areaName.contains("숲머리음식단지")) {
            return SUPMEORIFOODCOMPLEX;
        } else if (areaName.contains("디저트")) {
            return DESSERT;
        } else if (areaName.contains("전망좋은카페") || areaName.contains("전망 좋은 카페")) {
            return SCENIC_CAFE;
        }

        return null;
    }
}