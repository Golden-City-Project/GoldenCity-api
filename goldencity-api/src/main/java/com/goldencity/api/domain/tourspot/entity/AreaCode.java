package com.goldencity.api.domain.tourspot.entity;

// 경주시 관광 권역 Enum
public enum AreaCode {
    CITY_CENTER("경주시내권"),
    BOMUN("보문관광단지권"),
    BULGUKSA("불국사권"),
    NAMSAN("남산권"),
    EAST_SEA("동해권"),
    WEST_NORTH("서악북부권");

    // "경주시내권"등 저장하는 필드
    private final String description;

    // enum의 생성자에는 퍼블릭X, private default
    // 고정된 상수들의 집합이라서 퍼블릭으로 어디서든 new로 새로운 상수 만들어내면 안됨. 정해진 상수의 집합이니까.
    AreaCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}