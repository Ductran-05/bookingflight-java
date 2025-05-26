package com.bookingflight.app.auth;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.bookingflight.app.auth.LoginResponse.UserLogin;
import com.bookingflight.app.config.SecurityConfiguration;
import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.VerificationToken;
import com.bookingflight.app.dto.request.RegisterRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AccountResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
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
        final SecurityConfiguration securityConfig;

        @PostMapping("/login")
        public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
                // Nạp input gồm username/password vào Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                request.getUsername(), request.getPassword());
                // xác thực người dùng => cần viết hàm loadUserByUsername
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);
                Account currAccount = accountRepository.findByEmail(request.getUsername())
                                .orElseThrow(() -> new AppException(ErrorCode.AUTHENTICATION_FAILED));
                if (!currAccount.getEnabled())
                        throw new AppException(ErrorCode.ACCOUNT_INACTIVE);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // khởi tạo ResponseLogin
                UserLogin userLogin = UserLogin.builder()
                                .id(currAccount.getId())
                                .name(currAccount.getFullName())
                                .username(currAccount.getEmail())
                                .build();
                LoginResponse loginResponse = LoginResponse.builder()
                                .accessToken(securityUtil.createAccessToken(request.getUsername(), userLogin))
                                .user(userLogin)
                                .build();
                // tao refresh token
                String refreshToken = securityUtil.createRefreshToken(request.getUsername(), loginResponse);
                // update refresh token cho account
                accountService.updateAccountRefreshToken(refreshToken, request.getUsername());
                // set cookie refresh token
                ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(securityConfig.getRefreshTokenExpiration())
                                .build();

                // response data
                APIResponse<LoginResponse> response = APIResponse.<LoginResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(loginResponse)
                                .build();
                return ResponseEntity.ok()
                                .header("Set-Cookie", responseCookie.toString())
                                .body(response);
        }

        // lay nguoi dung da dang nhap tu cookie
        @GetMapping("/refresh")
        public ResponseEntity<APIResponse<LoginResponse>> refreshToken(
                        @CookieValue(name = "refreshToken", defaultValue = "") String refreshToken) {
                // check valid refresh token
                Jwt decodedTokenJwt = securityUtil.checkValidRefreshToken(refreshToken);
                String email = decodedTokenJwt.getSubject();
                // kiem tra co ton tai tai khoan dua vao usename va refresh token
                Account currAccount = accountService.findByEmailAndRefreshToken(email, refreshToken);
                LoginResponse.UserLogin userLogin = UserLogin.builder()
                                .id(currAccount.getId())
                                .name(currAccount.getFullName())
                                .username(currAccount.getEmail())
                                .build();
                LoginResponse loginResponse = LoginResponse.builder()
                                .accessToken(securityUtil.createAccessToken(email, userLogin))
                                .user(userLogin)
                                .build();
                // tao refresh token moi
                String newRefreshToken = securityUtil.createRefreshToken(email, loginResponse);
                // updateAccount
                accountService.updateAccountRefreshToken(newRefreshToken, email);
                // set new cookie
                ResponseCookie responseCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(securityConfig.getRefreshTokenExpiration())
                                .build();
                // response data
                APIResponse<LoginResponse> response = APIResponse.<LoginResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(loginResponse)
                                .build();
                return ResponseEntity.ok()
                                .header("Set-Cookie", responseCookie.toString())
                                .body(response);
        }

        // lay nguoi dung da dang nhap
        @GetMapping("/me")
        public ResponseEntity<APIResponse<UserLogin>> getUserLogin() {
                // lay email hien tai
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                Account currAccount = accountRepository.findByEmail(email)
                                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

                LoginResponse.UserLogin userLogin = UserLogin.builder()
                                .id(currAccount.getId())
                                .name(currAccount.getFullName())
                                .username(currAccount.getEmail())
                                .build();

                APIResponse<UserLogin> response = APIResponse.<UserLogin>builder()
                                .Code(200)
                                .Message("Success")
                                .data(userLogin)
                                .build();
                return ResponseEntity.ok(response);
        }

        @PostMapping("/register")
        public ResponseEntity<APIResponse<AccountResponse>> register(@Valid @RequestBody RegisterRequest request) {
                APIResponse<AccountResponse> apiResponse = APIResponse.<AccountResponse>builder()
                                .Code(201)
                                .Message("Register account successfully")
                                .data(accountService.registerUser(request))
                                .build();
                return ResponseEntity.ok().body(apiResponse);
        }

        @PostMapping("/logout")
        public ResponseEntity<APIResponse<String>> logout() {
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                accountService.updateAccountRefreshToken("", email);
                // set "" cookie refresh token
                ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                // response data
                APIResponse<String> response = APIResponse.<String>builder()
                                .Code(200)
                                .Message("Logout successfully")
                                .data(null)
                                .build();
                return ResponseEntity.ok()
                                .header("Set-Cookie", responseCookie.toString())
                                .body(response);
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
