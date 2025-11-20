package com.api.auth_service.exception;

public class UnathorizedAccessExeption extends RuntimeException {
    public UnathorizedAccessExeption(String message) {
        super(message);
    }
}