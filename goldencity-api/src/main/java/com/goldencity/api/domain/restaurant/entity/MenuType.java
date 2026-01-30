package com.goldencity.api.domain.restaurant.entity;

// 메뉴별 음식점 타입 (한식/중식/일식/양식)
public enum MenuType {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식");

    private final String description;

    MenuType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // API 응답의 CODE_NAME을 Enum으로 변환
    public static MenuType fromCodeName(String codeName) {
        if (codeName == null || codeName.trim().isEmpty()) {
            return null;
        }

        if (codeName.contains("한식")) {
            return KOREAN;
        } else if (codeName.contains("중식")) {
            return CHINESE;
        } else if (codeName.contains("일식")) {
            return JAPANESE;
        } else if (codeName.contains("양식")) {
            return WESTERN;
        }

        return null;
    }
}