package com.bookingflight.app.controller;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.AccountRequest;
import com.bookingflight.app.dto.request.UpdateAccountRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.service.AccountService;

import com.bookingflight.app.service.MyProfileService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
        private final AccountService accountService;
        private final PasswordEncoder passwordEncoder;
        private final MyProfileService myProfileService;

        @GetMapping("/{id}")
        public ResponseEntity<APIResponse<AccountResponse>> getAccountById(@PathVariable("id") String id) {

                APIResponse<AccountResponse> apiResponse = APIResponse.<AccountResponse>builder()
                                .Code(200)
                                .Message("Get account by id")
                                .data(accountService.getAccountByID(id))
                                .build();
                return ResponseEntity.ok().body(apiResponse);
        }

        @GetMapping
        public ResponseEntity<APIResponse<ResultPaginationDTO>> getAllAccounts(@Filter Specification<Account> spec,
                        Pageable pageable) {

                APIResponse<ResultPaginationDTO> apiResponse = APIResponse.<ResultPaginationDTO>builder()
                                .Code(200)
                                .Message("Get all accounts")
                                .data(accountService.getAllAccounts(spec, pageable))
                                .build();
                return ResponseEntity.ok().body(apiResponse);
        }

        @PostMapping(consumes = "multipart/form-data")
        public ResponseEntity<APIResponse<AccountResponse>> createAccount(
                @RequestPart("account") AccountRequest request,
                @RequestPart(value = "avatar", required = false) MultipartFile file) {

                // Password hash
                String hashPassword = passwordEncoder.encode(request.getPassword());
                request.setPassword(hashPassword);

                AccountResponse account = accountService.createAccount(request);

                if (file != null && !file.isEmpty()) {
                        account = accountService.uploadAvatar(account.getId(), file);
                }

                APIResponse<AccountResponse> apiResponse = APIResponse.<AccountResponse>builder()
                        .Code(201)
                        .Message("Create account")
                        .data(account)
                        .build();

                return ResponseEntity.ok().body(apiResponse);
        }
        @PutMapping("/{id}")
        public ResponseEntity<APIResponse<AccountResponse>> updateAccount(@PathVariable("id") String id,
                        @RequestBody UpdateAccountRequest request) {

                APIResponse<AccountResponse> apiResponse = APIResponse.<AccountResponse>builder()
                                .Code(200)
                                .Message("Update account by id")
                                .data(accountService.updateAccount(id, request))
                                .build();
                return ResponseEntity.ok().body(apiResponse);
        }

        @PostMapping("/{id}/avatar")
        public ResponseEntity<APIResponse<AccountResponse>> uploadAvatar(
                        @PathVariable("id") String id,
                        @RequestParam("file") MultipartFile file) {

                APIResponse<AccountResponse> apiResponse = APIResponse.<AccountResponse>builder()
                                .Code(200)
                                .Message("Avatar uploaded successfully")
                                .data(accountService.uploadAvatar(id, file))
                                .build();
                return ResponseEntity.ok().body(apiResponse);
        }

        @DeleteMapping("/{id}/avatar")
        public ResponseEntity<APIResponse<Void>> deleteAvatar(@PathVariable("id") String id) {
                accountService.deleteAvatar(id);

                APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                                .Code(200)
                                .Message("Avatar deleted successfully")
                                .build();
                return ResponseEntity.ok().body(apiResponse);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<APIResponse<Void>> deleteAccount(@PathVariable("id") String id) {
                APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                                .Code(200)
                                .Message("Delete account by id")
                                .build();
                accountService.deleteAccount(id);
                return ResponseEntity.ok().body(apiResponse);
        }

}