package com.filmnema.filmnema_api.dto;

public record FileUploadResponse(
		String originalFilename,
		String storedFilename,
		String publicUrl,
		String contentType,
		long size
) {
}