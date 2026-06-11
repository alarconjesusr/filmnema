package com.filmnema.filmnema_api.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record Movie(
        Long id,
        OffsetDateTime createdDate,
        OffsetDateTime modifiedDate,
        Boolean availableGlobally,
        String locale,
        String originalTitle,
        LocalDate releaseDate,
        Long runtime,
        String title
) {
}