package com.bookingflight.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.PermissionRequest;
import com.bookingflight.app.dto.response.PermissionResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.PermissionMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.experimental.FieldDefaults;

@Service
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionService {
    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;
    final ResultPanigationMapper resultPanigationMapper;

    public ResultPaginationDTO getAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<PermissionResponse> page = permissionRepository.findAll(spec, pageable)
                .map(permissionMapper::toPermissionResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public PermissionResponse getPermissionById(String id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        return permissionMapper.toPermissionResponse(permission);
    }

    public PermissionResponse createPermission(PermissionRequest permissionRequest) {

        if (permissionRepository.findByName(permissionRequest.getName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }

        Permission permission = permissionRepository.save(permissionMapper.toPermission(permissionRequest));
        return permissionMapper.toPermissionResponse(permission);
    }

    public PermissionResponse updatePermission(String id, PermissionRequest permissionRequest) {

        if (permissionRepository.findByName(permissionRequest.getName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        permission.setName(permissionRequest.getName());
        permission = permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    public void deletePermission(String id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        // Soft delete: set isDeleted to true and deletedAt to current timestamp
        permission.setIsDeleted(true);
        permission.setDeletedAt(java.time.LocalDateTime.now());
        permissionRepository.save(permission);
    }
}
