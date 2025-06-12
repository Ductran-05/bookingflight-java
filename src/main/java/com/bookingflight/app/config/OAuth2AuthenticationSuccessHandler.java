package com.bookingflight.app.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Role;
import com.bookingflight.app.mapper.AccountMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.RoleRepository;
import com.bookingflight.app.util.SecurityUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final SecurityUtil securityUtil;
    private final AccountMapper accountMapper;
    private final SecurityConfiguration securityConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        // Find or create user
        Account account = accountRepository.findByEmail(email).orElse(null);
        
        if (account == null) {
            // Create new account for Google user
            Role userRole = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Default USER role not found"));
            
            account = Account.builder()
                    .email(email)
                    .fullName(name)
                    .avatar(picture)
                    .enabled(true) 
                    .role(userRole)
                    .build();
            
            accountRepository.save(account);
        }

        // Gen JWT
        String accessToken = securityUtil.createAccessToken(email, accountMapper.toAccountResponse(account));
        String refreshToken = securityUtil.createRefreshToken(email, accountMapper.toAccountResponse(account));

        account.setRefreshToken(refreshToken);
        accountRepository.save(account);

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(securityConfig.getRefreshTokenExpiration())
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());


        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:5713/callback")
                .queryParam("token", accessToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
} 