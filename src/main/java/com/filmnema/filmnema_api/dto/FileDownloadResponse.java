package com.filmnema.filmnema_api.dto;

import org.springframework.core.io.Resource;

public record FileDownloadResponse(
		String storedFilename,
		String contentType,
		Resource resource
) {
}