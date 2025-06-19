package com.bookingflight.app.service;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.UpdateAccountRequest;
import com.bookingflight.app.dto.request.UpdatePasswordRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AccountMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.mapper.TicketMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.RoleRepository;
import com.bookingflight.app.repository.TicketRepository;
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
        final TicketRepository ticketRepository;
        final TicketMapper ticketMapper;
        final ResultPanigationMapper resultPanigationMapper;
        final RoleRepository roleRepository;
        final FileStorageService fileStorageService;

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

        public ResultPaginationDTO getAllTickets(Specification<Ticket> spec, Pageable pageable) {
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                Account currAccount = accountRepository.findByEmail(email)
                                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

                Specification<Ticket> finalSpec = spec
                                .and((root, query, cb) -> cb.equal(root.get("account"), currAccount));

                Page<TicketResponse> tickets = ticketRepository.findAll(finalSpec, pageable)
                                .map(ticketMapper::toTicketResponse);

                return resultPanigationMapper.toResultPanigationMapper(tickets);
        }

        public ResponseEntity<APIResponse<AccountResponse>> updateAccount(UpdateAccountRequest request) {
                String email = securityUtil.getCurrentUserLogin()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
                Account account = accountRepository.findByEmail(email)
                                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
                account.setFullName(request.getFullName());
                account.setPhone(request.getPhone());
                account.setEmail(request.getEmail());
                account.setAvatar(request.getAvatar());
                // account.setRole(roleRepository.findById(request.getRoleId()).get());
                accountRepository.save(account);
                return ResponseEntity.ok(
                                APIResponse.<AccountResponse>builder()
                                                .Code(200)
                                                .Message("Update account successfully")
                                                .data(accountMapper.toAccountResponse(account))
                                                .build());
        }

        public ResponseEntity<APIResponse<AccountResponse>> uploadAvatar(MultipartFile file) {
                String email = securityUtil.getCurrentUserLogin()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
                Account account = accountRepository.findByEmail(email)
                                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

                String fileName = fileStorageService.storeFile(file);
                String avatarUrl = fileStorageService.getFileUrl(fileName);
                account.setAvatar(avatarUrl);
                accountRepository.save(account);
                
                return ResponseEntity.ok(
                                APIResponse.<AccountResponse>builder()
                                                .Code(200)
                                                .Message("Avatar uploaded successfully")
                                                .data(accountMapper.toAccountResponse(account))
                                                .build());
        }

        public ResponseEntity<APIResponse<Void>> deleteAvatar() {
                String email = securityUtil.getCurrentUserLogin()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
                Account account = accountRepository.findByEmail(email)
                                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
                
                account.setAvatar(null);
                accountRepository.save(account);
                
                return ResponseEntity.ok(
                                APIResponse.<Void>builder()
                                                .Code(200)
                                                .Message("Avatar deleted successfully")
                                                .build());
        }

}
