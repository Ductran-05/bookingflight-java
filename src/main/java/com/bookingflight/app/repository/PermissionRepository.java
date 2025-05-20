package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {

    boolean existsByPermissionName(String permissionName);

}
