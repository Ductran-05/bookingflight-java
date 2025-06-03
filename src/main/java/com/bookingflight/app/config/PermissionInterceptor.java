package com.bookingflight.app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

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
        String httpMethod = request.getMethod();

        // Danh sách public GET
        if (httpMethod.equals("GET") && (requestURI.startsWith("/api/airports") ||
                requestURI.startsWith("/api/permissions") ||
                requestURI.startsWith("/api/cities") ||
                requestURI.startsWith("/api/airlines") ||
                requestURI.startsWith("/api/flights") ||
                requestURI.startsWith("/api/flights/seats") ||
                requestURI.startsWith("/api/files/") ||
                requestURI.startsWith("/api/payment/") ||
                requestURI.equals("/") // Trang chủ
        )) {
            return true;
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
                if (permission.getMethod().equalsIgnoreCase(httpMethod) &&
                        matcher.match(normalizedApiPath, normalizedRequestURI)) {
                    return true;
                }
            }

            throw new AppException(ErrorCode.FORBIDDEN);
        }

        return true;
    }

    private String normalizePath(String path) {
        // Thay {id} hoặc tương tự thành **
        path = path.replaceAll("\\{[^/]+}", "**");

        // Nếu không kết thúc bằng /** và không chứa wildcard thì thêm /** để gom nhóm
        if (!path.endsWith("/**") && !path.contains("*")) {
            path = path.replaceAll("/$", ""); // xóa dấu / cuối nếu có
            path += "/**";
        }

        return path;
    }
}