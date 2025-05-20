package com.bookingflight.app.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.domain.Role;
import com.bookingflight.app.dto.request.RoleRequest;
import com.bookingflight.app.dto.response.RoleResponse;
import com.bookingflight.app.repository.Permission_RoleRepostiory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Component
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleMapper {
    final Permission_RoleRepostiory permission_RoleRepostiory;
    final PermissionMapper permissionMapper;

    public Role toRole(RoleRequest roleRequest) {
        return Role.builder()
                .roleName(roleRequest.getRoleName())
                .build();
    }

    public RoleResponse toRoleResponse(Role role) {
        List<Permission> permissions = permission_RoleRepostiory.findByRole(role).stream()
                .map(permission_Role -> permission_Role.getPermission()).toList();
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .permissions(permissions.stream().map(permissionMapper::toPermissionResponse).toList())
                .build();
    }
}
