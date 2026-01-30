package com.goldencity.api.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

// 모든 API 응답의 공통 형식
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String warning;  // 옐로카드 메시지
    private String error;
    private LocalDateTime timestamp;

    private ApiResponse(boolean success, T data, String warning, String error) {
        this.success = success;
        this.data = data;
        this.warning = warning;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    // 성공 응답 (데이터만)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    // 성공 응답 (데이터 + 경고 메시지)
    public static <T> ApiResponse<T> success(T data, String warning) {
        return new ApiResponse<>(true, data, warning, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String error) {
        return new ApiResponse<>(false, null, null, error);
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getWarning() {
        return warning;
    }

    public String getError() {
        return error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}