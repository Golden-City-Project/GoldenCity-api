package com.goldencity.api.global.exception;

// 데이터 조회 실패 예외
public class DataNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DataNotFoundException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}