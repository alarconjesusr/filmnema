package com.filmnema.filmnema_api.dto;

import java.time.LocalDateTime;

public record EventResponse(
		Long id,
		String title,
		String description,
		LocalDateTime startsAt,
		LocalDateTime createdAt
) {
}