package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.dto.request.AccountRequest;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.jasypt.util.text.BasicTextEncryptor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    public Account toAccount(AccountRequest request) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

        textEncryptor.setPassword("mySecretKey");

        String encryptedPassword = textEncryptor.encrypt(request.getPassword());
        return Account.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(encryptedPassword)
                .role(request.getRole())
                .build();
    }

    public AccountResponse toAccountResponse(Account account) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

        textEncryptor.setPassword("mySecretKey");

        String decryptedPassword = textEncryptor.decrypt(account.getPassword());

        return AccountResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(decryptedPassword)
                .email(account.getEmail())
                .fullName(account.getFullName())
                .phone(account.getPhone())
                .role(account.getRole())
                .build();
    }

    public void updateAccount(Account account, AccountRequest request) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

        textEncryptor.setPassword("mySecretKey");

        String encryptedPassword = textEncryptor.encrypt(request.getPassword());
        account.setUsername(request.getUsername());
        account.setFullName(request.getFullName());
        account.setPhone(request.getPhone());
        account.setPassword(encryptedPassword);
        account.setEmail(request.getEmail());
        account.setRole(request.getRole());
    }

}