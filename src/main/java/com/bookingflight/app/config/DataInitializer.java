package com.bookingflight.app.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Role;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (!roleRepository.existsByRoleName("ADMIN")) {
            Role adminRole = Role.builder()
                    .roleName("ADMIN")
                    .build();
            roleRepository.save(adminRole);
        }
        // tạo account admin
        if (accountRepository.existsByRole(roleRepository.findByRoleName("ADMIN").get())) {
            return;
        }

        Account account = Account.builder()
                .email("admin")
                .password(passwordEncoder.encode("admin"))
                .role(roleRepository.findByRoleName("ADMIN").get())
                .enabled(true)
                .build();
        accountRepository.save(account);
    }
}
