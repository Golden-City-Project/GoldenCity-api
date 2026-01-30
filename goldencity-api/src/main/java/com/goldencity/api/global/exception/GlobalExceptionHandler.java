package com.goldencity.api.global.exception;

import com.goldencity.api.global.common.ApiResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 주의: jakarta.servlet.ServletException 임포트가 있다면 지워주세요.
// 스프링이 알아서 처리하게 두는 것이 가장 안전합니다.

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Slf4j 대신 직접 로거 선언 (중복 에러 방지)
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 지원하지 않는 HTTP 메서드 호출 시 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResponse<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("지원하지 않는 메서드 호출: {}", e.getMethod());

        return ApiResponse.error("지원하지 않는 HTTP 메서드입니다: " + e.getMethod());
    }

    /**
     * 데이터가 존재하지 않을 때 발생하는 커스텀 예외
     */
    @ExceptionHandler(DataNotFoundException.class)
    protected ApiResponse<Void> handleDataNotFoundException(DataNotFoundException e) {
        log.error("데이터 미존재 예외 발생: {}", e.getErrorCode().getMessage());
        return ApiResponse.error(e.getErrorCode().getMessage());
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ApiResponse<Void> handleException(Exception e) {
        log.error("예상치 못한 서버 에러 발생", e);
        return ApiResponse.error("서버 내부 에러가 발생했습니다.");
    }
}