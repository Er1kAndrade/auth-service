package com.api.auth_service.dto;

public record RefreshResponseDTO(
    String message,
    String accessToken
) {}