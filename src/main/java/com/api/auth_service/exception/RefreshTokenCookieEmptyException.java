package com.api.auth_service.exception;

public class RefreshTokenCookieEmptyException extends RuntimeException {
    public RefreshTokenCookieEmptyException(String message) {
        super(message);
    }
}