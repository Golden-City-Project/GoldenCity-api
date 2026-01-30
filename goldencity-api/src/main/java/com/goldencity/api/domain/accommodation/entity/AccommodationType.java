package com.goldencity.api.domain.accommodation.entity;

// 숙박 테마 타입
public enum AccommodationType {
    HOTEL("호텔"),
    GUEST_HOUSE("게스트하우스"),
    CONDO("콘도"),
    HANOK_HOTEL("한옥호텔"),
    CAMPING("오토캠핑,야영장");

    private final String description;

    AccommodationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // API 응답의 CODE_NAME을 Enum으로 변환
    public static AccommodationType fromCodeName(String codeName) {
        if (codeName == null || codeName.trim().isEmpty()) {
            return null;
        }

        if (codeName.contains("호텔") && !codeName.contains("한옥")) {
            return HOTEL;
        } else if (codeName.contains("게스트하우스")) {
            return GUEST_HOUSE;
        } else if (codeName.contains("콘도")) {
            return CONDO;
        } else if (codeName.contains("한옥호텔") || codeName.contains("한옥")) {
            return HANOK_HOTEL;
        } else if (codeName.contains("오토캠핑") || codeName.contains("야영장") || codeName.contains("캠핑")) {
            return CAMPING;
        }

        return null;
    }
}