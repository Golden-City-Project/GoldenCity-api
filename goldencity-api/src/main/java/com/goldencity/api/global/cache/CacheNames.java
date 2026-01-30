package com.goldencity.api.global.cache;

// 캐시 이름 상수 정의
public final class CacheNames {

    // 관광지
    public static final String TOURSPOTS = "tourspots";
    public static final String TOURSPOTS_BY_AREA = "tourspots-by-area";
    public static final String TOURSPOT_DETAIL = "tourspot-detail";
    public static final String TOURSPOTS_SEARCH = "tourspots-search";

    // 식당
    public static final String RESTAURANTS = "restaurants";
    public static final String RESTAURANTS_MENU = "restaurants-menu";
    public static final String RESTAURANTS_HOTPLACE = "restaurants-hotplace";
    public static final String RESTAURANT_DETAIL = "restaurant-detail";
    public static final String RESTAURANTS_SEARCH = "restaurants-search";

    // 숙박
    public static final String ACCOMMODATIONS = "accommodations";
    public static final String ACCOMMODATIONS_TYPE = "accommodations-type";
    public static final String ACCOMMODATION_DETAIL = "accommodation-detail";
    public static final String ACCOMMODATIONS_SEARCH = "accommodations-search";


    private CacheNames() {
        // 인스턴스 생성 방지
    }
}