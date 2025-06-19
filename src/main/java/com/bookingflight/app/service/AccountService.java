package com.bookingflight.app.service;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Role;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.domain.VerificationToken;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.AccountRequest;
import com.bookingflight.app.dto.request.RegisterRequest;
import com.bookingflight.app.dto.request.UpdateAccountRequest;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AccountMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.PaymentRepository;
import com.bookingflight.app.repository.ResetPasswordTokenRepository;
import com.bookingflight.app.repository.RoleRepository;
import com.bookingflight.app.repository.TicketRepository;
import com.bookingflight.app.repository.VerificationTokenRepository;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Data
@RequiredArgsConstructor
public class AccountService {

    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ResultPanigationMapper resultPanigationMapper;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TicketRepository ticketRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final PaymentRepository paymentRepository;

    public AccountResponse getAccountByID(String id) {
        return accountMapper.toAccountResponse(accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)));
    }

    public ResultPaginationDTO getAllAccounts(Specification<Account> spec, Pageable pageable) {
        Page<AccountResponse> page = accountRepository.findAll(spec, pageable).map(accountMapper::toAccountResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = accountMapper.toAccount(accountRequest);
        account.setEnabled(true);
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        } else if (accountRepository.existsByPhone(account.getPhone())) {
            throw new AppException(ErrorCode.ACCOUNT_PHONE_EXISTED);
        }

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    public AccountResponse updateAccount(String id, UpdateAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        account.setEnabled(true);
        accountMapper.updateAccount(account, request);

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Transactional
    public void deleteAccount(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        ticketRepository.removeAccountFromTickets(account);
        resetPasswordTokenRepository.deleteByAccount(account);
        paymentRepository.deleteByAccount(account);
        accountRepository.delete(account);
    }

    public AccountResponse registerUser(RegisterRequest request) {
        // Kiểm tra email đã tạo tài khoản thành công hay chua
        Account existingAccount = accountRepository.findOneByEmail(request.getEmail()).orElse(null);
        if (existingAccount != null) {
            if (existingAccount.getEnabled() == false) {
                // xoa verification token
                verificationTokenRepository.deleteByAccount(existingAccount);
                deleteAccount(existingAccount.getId());
            } else {
                throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
        }
        // Role USER
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        AccountRequest accountRequest = AccountRequest.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleId(userRole.getId())
                .build();

        Account account = accountMapper.toAccount(accountRequest);
        accountRepository.save(account);
        // Tạo token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .account(account)
                .expiryDate(LocalDateTime.now().plusDays(01))
                .build();
        verificationTokenRepository.save(verificationToken);

        // Gửi mail
        String link = "http://localhost:8080/api/auth/confirm?token=" + token;
        System.out.println(link);
        emailService.send(account.getEmail(), buildEmail(link));

        return accountMapper.toAccountResponse(account);
    }

    public AccountResponse uploadAvatar(String accountId, MultipartFile file) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        String avatarUrl = cloudinaryService.uploadImage(file);
        account.setAvatar(avatarUrl);
        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    public void deleteAvatar(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        account.setAvatar(null);
        accountRepository.save(account);
    }

    private String buildEmail(String link) {
        return "Chào bạn,\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản. Vui lòng nhấn vào liên kết dưới đây để xác thực email:\n"
                + link + "\n\n"
                + "Liên kết này sẽ hết hạn sau 24 giờ.\n\n"
                + "Trân trọng.";
    }

    public void updateAccountRefreshToken(String refreshToken, String email) {
        Account currAccount = accountRepository.findByEmail(email).get();
        if (currAccount != null) {
            currAccount.setRefreshToken(refreshToken);
            accountRepository.save(currAccount);
        }
    }

    public Account findByEmailAndRefreshToken(String email, String refreshToken) {
        return accountRepository.findByEmailAndRefreshToken(email, refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_INVALID));
    }
}
