package com.bookingflight.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.dto.request.UpdatePasswordRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AccountMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.util.SecurityUtil;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyProfileService {

    final PasswordEncoder passwordEncoder;
    final AccountRepository accountRepository;
    final SecurityUtil securityUtil;
    final AccountMapper accountMapper;

    public ResponseEntity<APIResponse<Void>> updatePassword(UpdatePasswordRequest request) {
        String email = securityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
        return ResponseEntity.ok(
                APIResponse.<Void>builder()
                        .Code(200)
                        .Message("Password updated successfully")
                        .build());
    }

    public ResponseEntity<APIResponse<AccountResponse>> getUserLogin() {
        // lay email hien tai
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        Account currAccount = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        // response data
        APIResponse<AccountResponse> response = APIResponse.<AccountResponse>builder()
                .Code(200)
                .Message("Success")
                .data(accountMapper.toAccountResponse(currAccount))
                .build();
        return ResponseEntity.ok(response);
    }

}
