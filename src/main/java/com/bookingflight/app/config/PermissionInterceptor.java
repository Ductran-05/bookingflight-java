package com.bookingflight.app.config;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.domain.Role;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.Permission_RoleRepostiory;
import com.bookingflight.app.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Permission_RoleRepostiory permission_RoleRepostiory;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        String normalizedPath = normalizePath(requestURI);

        if (PublicEndpoints.isPublic(normalizedPath, httpMethod)) {
            return true;
        }
        // Danh sách public GET
        if (httpMethod.equals("GET")) {
            if (PublicEndpoints.GET_METHODS.contains(normalizedPath)) {
                return true;
            }
        }

        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (!email.isEmpty()) {
            Account account = accountRepository.findByEmail(email).orElse(null);

            if (account == null || account.getRole() == null) {
                throw new AppException(ErrorCode.ROLE_NOT_FOUND);
            }

            if ("ADMIN".equals(account.getRole().getRoleName())) {
                return true;
            }

            Role role = account.getRole();
            List<Permission> permissions = permission_RoleRepostiory.findByRole(role).stream()
                    .map(permission_Role -> permission_Role.getPermission()).toList();

            AntPathMatcher matcher = new AntPathMatcher();
            String normalizedRequestURI = normalizePath(requestURI);

            for (Permission permission : permissions) {
                String normalizedApiPath = normalizePath(permission.getApiPath());
                if (permission.getMethod().equals(httpMethod.name()) &&
                        matcher.match(normalizedApiPath, normalizedRequestURI)) {
                    return true;
                }
            }

            throw new AppException(ErrorCode.FORBIDDEN);
        }

        return true;
    }

    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/**";
        }

        String[] segments = path.split("/");
        StringBuilder normalized = new StringBuilder();

        for (String segment : segments) {
            if (segment.isEmpty())
                continue;

            if (isUUID(segment) || isNumeric(segment)) {
                normalized.append("/**");
                break; // Dừng tại đoạn động
            } else {
                normalized.append("/").append(segment);
            }
        }

        if (!normalized.toString().contains("**")) {
            normalized.append("/**");
        }

        return normalized.toString();
    }

    private boolean isUUID(String str) {
        return str
                .matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
    }

    private boolean isNumeric(String str) {
        return str.matches("^\\d+$");
    }

}