package com.bookingflight.app.service;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.dto.request.AccountRequest;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AccountMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.PlaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public AccountResponse getAccountByID(String id) {
        return accountMapper.toAccountResponse(accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)));
    }
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                        .map(accountMapper::toAccountResponse)
                        .collect(Collectors.toList());
    }
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = accountMapper.toAccount(accountRequest);
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }
    public AccountResponse updateAccount(String id, AccountRequest accountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        accountMapper.updateAccount(account, accountRequest);

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }
    public void deleteAccount(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        accountRepository.delete(account);
    }
}
