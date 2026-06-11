package com.filmnema.filmnema_api.dto;

import java.time.LocalDate;

public record MovieUpdateRequest(
        Boolean availableGlobally,
        String locale,
        String originalTitle,
        LocalDate releaseDate,
        Long runtime,
        String title
) {
}