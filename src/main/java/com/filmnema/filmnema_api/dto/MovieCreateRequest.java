package com.filmnema.filmnema_api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MovieCreateRequest(
        @NotNull
        Boolean availableGlobally,
        @NotBlank
        @Size(max = 10)
        String locale,
        @NotBlank
        @Size(max = 255)
        String originalTitle,
        LocalDate releaseDate,
        @NotNull
        @Positive
        Long runtime,
        @NotBlank
        @Size(max = 255)
        String title
) {
}