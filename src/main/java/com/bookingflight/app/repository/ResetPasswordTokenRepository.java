package com.bookingflight.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bookingflight.app.domain.ResetPasswordToken;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {
    Optional<ResetPasswordToken> findByToken(String token);
}
