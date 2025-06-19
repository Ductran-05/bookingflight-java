package com.bookingflight.app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;
    
    private final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};
    private final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public String uploadImage(MultipartFile file) {
        // Validate file
        validateFile(file);

        try {
            // Generate unique public ID
            String publicId = "bookingflight/avatars/" + UUID.randomUUID().toString();
            
            // Upload to Cloudinary with transformation
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", "bookingflight/avatars",
                    "resource_type", "image",
                    "transformation", "c_fill,w_300,h_300,q_auto"
                ));
            
            return (String) uploadResult.get("secure_url");
        } catch (IOException ex) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }
        
        try {
            // Extract public ID from URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (IOException ex) {
            // Log the error but don't throw exception for delete operations
            System.err.println("Could not delete image from Cloudinary: " + imageUrl);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }

        String fileExtension = getFileExtension(originalFileName);
        boolean isValidExtension = false;
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equalsIgnoreCase(fileExtension)) {
                isValidExtension = true;
                break;
            }
        }

        if (!isValidExtension) {
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        
        // Extract public ID from Cloudinary URL
        // URL format: https://res.cloudinary.com/cloud_name/image/upload/v1234567890/folder/public_id.ext
        try {
            String[] parts = imageUrl.split("/");
            if (parts.length >= 3) {
                // Find the version part (starts with 'v' followed by numbers)
                int versionIndex = -1;
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].matches("v\\d+")) {
                        versionIndex = i;
                        break;
                    }
                }
                
                if (versionIndex != -1 && versionIndex + 1 < parts.length) {
                    // Reconstruct public ID from the parts after version
                    StringBuilder publicId = new StringBuilder();
                    for (int i = versionIndex + 1; i < parts.length; i++) {
                        if (i > versionIndex + 1) {
                            publicId.append("/");
                        }
                        // Remove file extension from the last part
                        String part = parts[i];
                        if (i == parts.length - 1 && part.contains(".")) {
                            part = part.substring(0, part.lastIndexOf("."));
                        }
                        publicId.append(part);
                    }
                    return publicId.toString();
                }
            }
        } catch (Exception e) {
            System.err.println("Error extracting public ID from URL: " + imageUrl);
        }
        
        return null;
    }
} 