package com.bookingflight.app.mapper;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.dto.request.PermissionRequest;
import com.bookingflight.app.dto.response.PermissionResponse;
import com.bookingflight.app.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Component
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionMapper {
    final PermissionRepository permissionRepository;

    public Permission toPermission(PermissionRequest permissionRequest) {
        return Permission.builder()
                .apiPath(permissionRequest.getApiPath())
                .method(permissionRequest.getMethod())
                .model(permissionRequest.getModel())
                .name(permissionRequest.getName())
                .build();
    }

    public PermissionResponse toPermissionResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .apiPath(permission.getApiPath())
                .method(permission.getMethod())
                .model(permission.getModel())
                .build();
    }
}
