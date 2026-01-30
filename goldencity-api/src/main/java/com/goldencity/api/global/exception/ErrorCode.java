package com.goldencity.api.global.exception;

// 에러 코드 정의
public enum ErrorCode {
    // 공통
    INTERNAL_SERVER_ERROR("COMMON_001", "서버 내부 오류가 발생했습니다"),
    INVALID_INPUT_VALUE("COMMON_002", "잘못된 입력 값입니다"),
    METHOD_NOT_ALLOWED("COMMON_003", "허용되지 않는 HTTP 메서드입니다"),
    INVALID_TYPE_VALUE("COMMON_004", "잘못된 타입의 값입니다"),

    // 데이터 조회
    TOUR_SPOT_NOT_FOUND("DATA_001", "관광지를 찾을 수 없습니다"),
    RESTAURANT_NOT_FOUND("DATA_002", "식당을 찾을 수 없습니다"),
    ACCOMMODATION_NOT_FOUND("DATA_003", "숙박업소를 찾을 수 없습니다"),
    HERITAGE_NOT_FOUND("DATA_004", "문화재를 찾을 수 없습니다"),

    // 외부 API
    EXTERNAL_API_ERROR("API_001", "외부 API 호출에 실패했습니다"),
    EXTERNAL_API_TIMEOUT("API_002", "외부 API 응답 시간이 초과되었습니다"),
    WEATHER_API_ERROR("API_003", "날씨 API 호출에 실패했습니다"),
    WEATHER_API_PARSE_ERROR("API_004", "날씨 API 응답 파싱에 실패했습니다"),

    // 배치
    BATCH_EXECUTION_FAILED("BATCH_001", "배치 실행에 실패했습니다"),
    BATCH_DATA_SYNC_FAILED("BATCH_002", "데이터 동기화에 실패했습니다"),

    // 캐시
    CACHE_ERROR("CACHE_001", "캐시 처리 중 오류가 발생했습니다"),
    CACHE_EVICTION_FAILED("CACHE_002", "캐시 삭제에 실패했습니다");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}