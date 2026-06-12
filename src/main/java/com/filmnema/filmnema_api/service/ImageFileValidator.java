package com.filmnema.filmnema_api.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.filmnema.filmnema_api.exception.FilmnemaException;

@Component
public class ImageFileValidator {

	private static final byte[] PNG_MAGIC = new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
	private static final byte[] JPEG_MAGIC_START = new byte[] {(byte) 0xFF, (byte) 0xD8};
	private static final byte[] JPEG_MAGIC_END = new byte[] {(byte) 0xFF, (byte) 0xD9};
	private static final byte[] GIF_87A_MAGIC = "GIF87a".getBytes(StandardCharsets.US_ASCII);
	private static final byte[] GIF_89A_MAGIC = "GIF89a".getBytes(StandardCharsets.US_ASCII);

	public void validate(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new FilmnemaException("File is required.");
		}

		String contentType = file.getContentType();
		String normalizedContentType = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);
		if (!isAllowedImageContentType(normalizedContentType)) {
			throw new FilmnemaException("Only image files are allowed.");
		}

		byte[] fileBytes = readBytes(file);
		if (!isImage(fileBytes)) {
			throw new FilmnemaException("Only valid image files are allowed.");
		}
	}

	private byte[] readBytes(MultipartFile file) {
		try {
			return file.getBytes();
		} catch (IOException exception) {
			throw new FilmnemaException("Unable to read uploaded file.");
		}
	}

	private boolean isAllowedImageContentType(String contentType) {
		return "image/jpeg".equals(contentType)
				|| "image/jpg".equals(contentType)
				|| "image/png".equals(contentType)
				|| "image/gif".equals(contentType);
	}

	private boolean isImage(byte[] fileBytes) {
		return matchesPrefix(fileBytes, PNG_MAGIC)
				|| (matchesPrefix(fileBytes, JPEG_MAGIC_START) && matchesSuffix(fileBytes, JPEG_MAGIC_END))
				|| matchesPrefix(fileBytes, GIF_87A_MAGIC)
				|| matchesPrefix(fileBytes, GIF_89A_MAGIC);
	}

	private boolean matchesPrefix(byte[] fileBytes, byte[] expectedPrefix) {
		if (fileBytes.length < expectedPrefix.length) {
			return false;
		}

		for (int index = 0; index < expectedPrefix.length; index++) {
			if (fileBytes[index] != expectedPrefix[index]) {
				return false;
			}
		}

		return true;
	}

	private boolean matchesSuffix(byte[] fileBytes, byte[] expectedSuffix) {
		if (fileBytes.length < expectedSuffix.length) {
			return false;
		}

		int offset = fileBytes.length - expectedSuffix.length;
		for (int index = 0; index < expectedSuffix.length; index++) {
			if (fileBytes[offset + index] != expectedSuffix[index]) {
				return false;
			}
		}

		return true;
	}
}