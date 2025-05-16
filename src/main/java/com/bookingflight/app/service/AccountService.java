package com.bookingflight.app.service;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.dto.request.AccountRequest;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AccountMapper;
import com.bookingflight.app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public List<AccountResponse> getAllAccounts(Specification<Account> spec, Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return accounts.getContent().stream().map(accountMapper::toAccountResponse).collect(Collectors.toList());
    }

    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = accountMapper.toAccount(accountRequest);

        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        } else if (accountRepository.existsByPhone(account.getPhone())) {
            throw new AppException(ErrorCode.ACCOUNT_PHONE_EXISTED);
        } else if (accountRepository.existsByUsername(account.getUsername())) {
            throw new AppException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
        }

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
