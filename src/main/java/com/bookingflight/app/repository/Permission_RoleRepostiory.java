package com.bookingflight.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.domain.Permission_Role;
import com.bookingflight.app.domain.Role;

public interface Permission_RoleRepostiory extends JpaRepository<Permission_Role, String> {

    List<Permission_Role> findByRole(Role role);

    void deleteAllByRole(Role role);

    List<Permission_Role> findAllByRole(Role role);

    boolean existsByPermissionAndRole(Permission permission, Role adminRole);

}
