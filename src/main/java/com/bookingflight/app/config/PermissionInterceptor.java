package com.bookingflight.app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);
        // check permission
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        System.out.println(">>> email= " + email);
        if (!email.isEmpty()) {
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
            if (account.getRole().getRoleName().equals("ADMIN")) {
                return true;
            }
            if (account != null) {
                Role role = account.getRole();
                if (role != null) {
                    List<Permission> permissions = permission_RoleRepostiory.findAllByRole(role).stream()
                            .map(permission_Role -> permission_Role.getPermission()).toList();
                    if (permissions != null) {
                        for (Permission permission : permissions) {
                            if (permission.getApiPath().equals(path) && permission.getMethod().equals(httpMethod)) {
                                return true;
                            }
                        }
                        throw new AppException(ErrorCode.UNAUTHORIZED);
                    }
                }
            }
        }
        return true;
    }
}
