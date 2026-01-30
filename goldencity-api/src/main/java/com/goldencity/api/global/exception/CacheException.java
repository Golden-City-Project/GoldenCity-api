package com.goldencity.api.global.exception;

// 캐시 처리 예외
public class CacheException extends RuntimeException {

    private final ErrorCode errorCode;

    public CacheException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CacheException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}