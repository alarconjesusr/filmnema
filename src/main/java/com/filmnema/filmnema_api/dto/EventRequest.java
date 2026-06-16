package com.filmnema.filmnema_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record EventRequest(
		@NotBlank
		@Size(max = 150)
		String title,
		@Size(max = 2000)
		String description,
		@NotNull
		@FutureOrPresent
		LocalDateTime startsAt
) {
}