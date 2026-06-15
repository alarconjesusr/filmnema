package com.filmnema.filmnema_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TestRequest(
        @NotBlank
        @Size(max = 100)
        String name,
        @NotBlank
        @Email
        @Size(max = 255)
        String email,
        @Size(max = 500)
        String message
) {
}