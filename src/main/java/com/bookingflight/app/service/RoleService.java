package com.bookingflight.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.domain.Permission_Role;
import com.bookingflight.app.domain.Role;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.RoleRequest;
import com.bookingflight.app.dto.response.RoleResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.mapper.RoleMapper;
import com.bookingflight.app.repository.PermissionRepository;
import com.bookingflight.app.repository.Permission_RoleRepostiory;
import com.bookingflight.app.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Service
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleService {
    final RoleRepository roleRepository;
    final RoleMapper roleMapper;
    final Permission_RoleRepostiory permission_RoleRepostiory;
    final PermissionService permissionService;
    final PermissionRepository permissionRepository;
    private final ResultPanigationMapper resultPanigationMapper;

    public ResultPaginationDTO getAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<RoleResponse> page = roleRepository.findAll(spec, pageable)
                .map(roleMapper::toRoleResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public RoleResponse getRole(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse createRole(RoleRequest roleRequest) {

        if (roleRepository.existsByRoleName(roleRequest.getRoleName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        Role role = roleMapper.toRole(roleRequest);
        Role savedRole = roleRepository.save(role);

        for (String permissionId : roleRequest.getPermissionId()) {
            Permission savedPermission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
            Permission_Role permission_Role = Permission_Role.builder()
                    .permission(savedPermission)
                    .role(savedRole)
                    .build();
            permission_RoleRepostiory.save(permission_Role);
        }

        return roleMapper.toRoleResponse(savedRole);
    }

    @Transactional
    public void deleteRole(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        permission_RoleRepostiory.deleteAllByRole(role);

        roleRepository.delete(role);
    }

    @Transactional
    public RoleResponse updateRole(String id, RoleRequest roleRequest) {

        permission_RoleRepostiory.deleteAllByRole(
                roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));

        for (String permissionId : roleRequest.getPermissionId()) {
            Permission savedPermission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
            Permission_Role permission_Role = Permission_Role.builder()
                    .permission(savedPermission)
                    .role(roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                    .build();
            permission_RoleRepostiory.save(permission_Role);
        }

        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        role.setRoleName(roleRequest.getRoleName());
        Role savedRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(savedRole);
    }
}
