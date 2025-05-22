package com.bookingflight.app.auth;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.VerificationToken;
import com.bookingflight.app.dto.request.RegisterRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.VerificationTokenRepository;
import com.bookingflight.app.service.AccountService;
import com.bookingflight.app.util.SecurityUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    final AccountRepository accountRepository;

    final AccountService accountService;
    final SecurityUtil securityUtil;
    final AuthenticationManagerBuilder authenticationManagerBuilder;
    final VerificationTokenRepository verificationTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // tạo token cho người dùng
        String accessToken = securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        APIResponse<LoginResponse> apiResponse = new APIResponse<>();
        apiResponse.setMessage("Login successful");
        apiResponse.setCode(200);
        apiResponse.setData(loginResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        accountService.registerUser(request);
        return ResponseEntity.ok("Đăng ký thành công! Vui lòng kiểm tra email để xác thực.");
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);

        if (optionalToken.isEmpty())
            return "Invalid token";

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        Account account = verificationToken.getAccount();
        account.setEnabled(true);
        accountRepository.save(account);

        verificationTokenRepository.delete(verificationToken); // Optionally remove token
        return "Email confirmed. Account activated.";
    }
}
