package com.bookingflight.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Try to determine file's content type
                String contentType = null;
                try {
                    contentType = java.nio.file.Files.probeContentType(filePath);
                } catch (IOException ex) {
                    // Could not determine content type, default to binary
                    contentType = "application/octet-stream";
                }

                // Fallback to the default content type if type could not be determined
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (Exception ex) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }
    }
} 