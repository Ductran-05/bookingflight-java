package com.bookingflight.app.controller;

import com.bookingflight.app.dto.request.AccountRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

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
    public ResponseEntity<APIResponse<List<AccountResponse>>> getAllAccounts() {
        APIResponse<List<AccountResponse>> apiResponse = APIResponse.<List<AccountResponse>>builder()
                .Code(200)
                .Message("Get all accounts")
                .data(accountService.getAllAccounts())
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping
    public ResponseEntity<APIResponse<AccountResponse>> createAccount(@RequestBody @Valid AccountRequest request) {

        APIResponse<AccountResponse> apiResponse = APIResponse.<AccountResponse>builder()
                .Code(201)
                .Message("Create account")
                .data(accountService.createAccount(request))
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<AccountResponse>> updateAccount(@PathVariable("id") String id,
            @RequestBody AccountRequest request) {
        APIResponse<AccountResponse> apiResponse = APIResponse.<AccountResponse>builder()
                .Code(200)
                .Message("Update account by id")
                .data(accountService.updateAccount(id, request))
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