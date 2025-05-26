package com.bookingflight.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.VerificationToken;

import jakarta.transaction.Transactional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);

    @Transactional
    void deleteByAccount(Account existingAccount);
}