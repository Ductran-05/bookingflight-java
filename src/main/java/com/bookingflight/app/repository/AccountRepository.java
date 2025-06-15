package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Account> findByEmail(String email);

    Optional<Account> findOneByEmail(String email);

    Optional<Account> findByEmailAndRefreshToken(String email, String refreshToken);

    boolean existsByRole(Role role);

    void deleteAllByRole(Role role);

}
