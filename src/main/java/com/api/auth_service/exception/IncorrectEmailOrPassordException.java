package com.api.auth_service.exception;

public class IncorrectEmailOrPassordException extends RuntimeException {
    public IncorrectEmailOrPassordException(String message) {
        super(message);
    }
}
