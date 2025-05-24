package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.dto.request.AccountRequest;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public Account toAccount(AccountRequest request) {

        return Account.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(request.getPassword())
                .avatar(request.getAvatar())
                .role(roleRepository.findById(request.getRoleId()).get())
                .build();
    }

    public AccountResponse toAccountResponse(Account account) {

        return AccountResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .fullName(account.getFullName())
                .phone(account.getPhone())
                .avatar(account.getAvatar())
                .role(roleMapper.toRoleResponse(account.getRole()))
                .build();
    }

    public void updateAccount(Account account, AccountRequest request) {

        account.setFullName(request.getFullName());
        account.setPhone(request.getPhone());
        account.setPassword(request.getPassword());
        account.setEmail(request.getEmail());
        account.setAvatar(request.getAvatar());
        account.setRole(roleRepository.findById(request.getRoleId()).get());
    }

}