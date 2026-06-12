package com.filmnema.filmnema_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import com.filmnema.filmnema_api.config.FileStorageProperties;
import com.filmnema.filmnema_api.dto.FileUploadResponse;
import com.filmnema.filmnema_api.exception.FilmnemaException;

class LocalFileStorageServiceTest {

	@TempDir
	Path tempDir;

	@Test
	void store_savesImageUsingUuidAndExtension() throws Exception {
		LocalFileStorageService service = new LocalFileStorageService(fileStorageProperties(tempDir), new ImageFileValidator());
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"../poster.png",
				"image/png",
				createPngBytes()
		);

		FileUploadResponse response = service.storeImage(file);

		assertEquals("poster.png", response.originalFilename());
		assertEquals("image/png", response.contentType());
		assertTrue(response.storedFilename().matches("[0-9a-fA-F\\-]{36}\\.png"));
		assertEquals("/" + response.storedFilename(), response.publicUrl());
		assertTrue(Files.exists(tempDir.resolve(response.storedFilename())));
	}

	@Test
	void store_savesGifUsingUuidAndExtension() {
		LocalFileStorageService service = new LocalFileStorageService(fileStorageProperties(tempDir), new ImageFileValidator());
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"preview.gif",
				"image/gif",
				Base64.getDecoder().decode("R0lGODlhAQABAIAAAP///wAAACH5BAEAAAEALAAAAAABAAEAAAICRAEAOw==")
		);

		FileUploadResponse response = service.storeImage(file);

		assertTrue(response.storedFilename().matches("[0-9a-fA-F\\-]{36}\\.gif"));
		assertEquals("/" + response.storedFilename(), response.publicUrl());
		assertTrue(Files.exists(tempDir.resolve(response.storedFilename())));
	}


	@Test
	void store_rejectsNonImageFiles() {
		LocalFileStorageService service = new LocalFileStorageService(fileStorageProperties(tempDir), new ImageFileValidator());
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"document.txt",
				"text/plain",
				"plain text".getBytes()
		);

		assertThrows(FilmnemaException.class, () -> service.storeImage(file));
		assertFalse(Files.exists(tempDir.resolve("document.txt")));
	}

	@Test
	void store_acceptsAnyFileGenerically() {
		LocalFileStorageService service = new LocalFileStorageService(fileStorageProperties(tempDir), new ImageFileValidator());
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"document.txt",
				"text/plain",
				"plain text".getBytes()
		);

		FileUploadResponse response = service.store(file);

		assertEquals("document.txt", response.originalFilename());
		assertTrue(response.storedFilename().matches("[0-9a-fA-F\\-]{36}\\.txt"));
		assertTrue(Files.exists(tempDir.resolve(response.storedFilename())));
	}

	private FileStorageProperties fileStorageProperties(Path directory) {
		FileStorageProperties fileStorageProperties = new FileStorageProperties();
		fileStorageProperties.setDir(directory.toString());
		return fileStorageProperties;
	}

	private byte[] createPngBytes() throws Exception {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", outputStream);
		return outputStream.toByteArray();
	}
}