package com.bookingflight.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bookingflight.app.domain.Role;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    boolean existsByRoleName(String roleName);

    Optional<Role> findByRoleName(String string);

}
