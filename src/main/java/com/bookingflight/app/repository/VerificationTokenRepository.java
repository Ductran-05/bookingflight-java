package com.bookingflight.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByAccount(Account existingAccount);
}