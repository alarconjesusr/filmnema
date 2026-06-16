package com.filmnema.filmnema_api.dto;

public record TokenResponse(
        String token,
        String tokenType
) {
}