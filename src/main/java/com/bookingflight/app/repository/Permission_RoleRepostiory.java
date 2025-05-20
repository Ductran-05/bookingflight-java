package com.bookingflight.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookingflight.app.domain.Permission_Role;
import com.bookingflight.app.domain.Role;

public interface Permission_RoleRepostiory extends JpaRepository<Permission_Role, String> {

    List<Permission_Role> findByRole(Role role);

    void deleteAllByRole(Role role);

}
