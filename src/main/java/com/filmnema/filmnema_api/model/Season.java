package com.filmnema.filmnema_api.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record Season(
	Long id,
	OffsetDateTime createdDate,
	OffsetDateTime modifiedDate,
	String originalTitle,
	LocalDate releaseDate,
	Long runtime,
	Integer seasonNumber,
	String title,
	Long tvShowId
) {
}
