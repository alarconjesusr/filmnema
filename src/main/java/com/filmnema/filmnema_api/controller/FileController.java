package com.filmnema.filmnema_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.filmnema.filmnema_api.dto.FileUploadResponse;
import com.filmnema.filmnema_api.service.IFileStorageService;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

	private final IFileStorageService fileStorageService;

	public FileController(IFileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		FileUploadResponse response = fileStorageService.storeImage(file);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}