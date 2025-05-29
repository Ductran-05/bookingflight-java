package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {

    boolean findByName(String name);

    boolean existsByApiPathAndMethod(String url, String name);

}
