package com.bookingflight.app.service;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.ResetPasswordToken;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.ResetPasswordTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final AccountRepository accountRepository;
    private final ResetPasswordTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void processForgotPassword(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        String token = UUID.randomUUID().toString();
        ResetPasswordToken resetToken = ResetPasswordToken.builder()
                .token(token)
                .account(account)
                .expireAt(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .build();
        tokenRepository.save(resetToken);

        sendResetEmail(account.getEmail(), token);
    }

    private void sendResetEmail(String toEmail, String token) {
        String resetLink = "http://localhost:8080/reset-password/changePasword?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset your password");
        message.setText("Click this link to reset your password (valid for 30 minutes): " + resetLink);
        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        ResetPasswordToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_INVALID));

        if (resetToken.isUsed() || resetToken.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        Account account = resetToken.getAccount();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
