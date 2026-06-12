package com.filmnema.filmnema_api.service;

import com.filmnema.filmnema_api.dto.FileUploadResponse;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
    FileUploadResponse store(MultipartFile file);

    FileUploadResponse storeImage(MultipartFile file);
}