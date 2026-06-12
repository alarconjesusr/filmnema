package com.filmnema.filmnema_api.service;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.filmnema.filmnema_api.config.FileStorageProperties;
import com.filmnema.filmnema_api.dto.FileUploadResponse;
import com.filmnema.filmnema_api.exception.FilmnemaException;

@Service
public class LocalFileStorageService implements IFileStorageService {

	private final Path storageDirectory;
	private final ImageFileValidator imageFileValidator;

	public LocalFileStorageService(FileStorageProperties fileStorageProperties, ImageFileValidator imageFileValidator) {
		String configuredPath = Objects.requireNonNullElse(fileStorageProperties.getDir(), "src/main/resources/imgs");
		this.storageDirectory = Path.of(configuredPath).toAbsolutePath().normalize();
		this.imageFileValidator = imageFileValidator;

		try {
			Files.createDirectories(this.storageDirectory);
		} catch (IOException exception) {
			throw new FilmnemaException("Unable to initialize file storage directory.");
		}
	}

	@Override
	public FileUploadResponse store(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new FilmnemaException("File is required.");
		}

		byte[] fileBytes;
		try {
			fileBytes = file.getBytes();
		} catch (IOException exception) {
			throw new FilmnemaException("Unable to read uploaded file.");
		}

		String originalFilename = StringUtils.cleanPath(Objects.requireNonNullElse(file.getOriginalFilename(), ""));
		String safeFilename = extractSafeFilename(originalFilename);
		String extension = extractExtension(safeFilename, file.getContentType());
		String storedFilename = UUID.randomUUID() + extension;
		Path targetPath = resolveTargetPath(storedFilename);

		try {
			Files.copy(new ByteArrayInputStream(fileBytes), targetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException exception) {
			throw new FilmnemaException("Unable to store uploaded file.");
		}

		return new FileUploadResponse(
				safeFilename,
				storedFilename,
				"/" + storedFilename,
				file.getContentType(),
				file.getSize()
		);
	}

	@Override
	public FileUploadResponse storeImage(MultipartFile file) {
		imageFileValidator.validate(file);

		return store(file);
	}


	private Path resolveTargetPath(String storedFilename) {
		try {
			String safeFilename = Path.of(storedFilename).getFileName().toString();
			Path resolvedPath = storageDirectory.resolve(safeFilename).normalize();
			if (!resolvedPath.startsWith(storageDirectory)) {
				throw new FilmnemaException("Invalid file path.");
			}

			return resolvedPath;
		} catch (InvalidPathException exception) {
			throw new FilmnemaException("Invalid file name.");
		}
	}

	private String extractSafeFilename(String originalFilename) {
		if (!StringUtils.hasText(originalFilename)) {
			return "file";
		}

		return Path.of(originalFilename).getFileName().toString();
	}

	private String extractExtension(String originalFilename, String contentType) {
		int extensionSeparatorIndex = originalFilename.lastIndexOf('.');
		if (extensionSeparatorIndex >= 0 && extensionSeparatorIndex < originalFilename.length() - 1) {
			return originalFilename.substring(extensionSeparatorIndex).toLowerCase(Locale.ROOT);
		}

		if (contentType == null || contentType.isBlank()) {
			return ".bin";
		}

		int contentTypeSeparatorIndex = contentType.indexOf('/');
		if (contentTypeSeparatorIndex >= 0 && contentTypeSeparatorIndex < contentType.length() - 1) {
			String subtype = contentType.substring(contentTypeSeparatorIndex + 1).toLowerCase(Locale.ROOT);
			subtype = subtype.replace("+xml", "");
			return "." + subtype;
		}

		return ".img";
	}
}