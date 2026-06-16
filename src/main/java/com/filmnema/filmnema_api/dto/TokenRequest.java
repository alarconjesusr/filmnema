package com.filmnema.filmnema_api.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}