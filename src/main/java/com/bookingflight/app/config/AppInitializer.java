package com.bookingflight.app.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.domain.Permission_Role;
import com.bookingflight.app.domain.Role;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.PermissionRepository;
import com.bookingflight.app.repository.Permission_RoleRepostiory;
import com.bookingflight.app.repository.RoleRepository;

import java.util.*;

@Component
public class AppInitializer {

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final RequestMappingHandlerMapping handlerMapping;
    private final Permission_RoleRepostiory permission_RoleRepostiory;

    public AppInitializer(RoleRepository roleRepository, AccountRepository accountRepository,
            PasswordEncoder passwordEncoder, PermissionRepository permissionRepository,
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
            Permission_RoleRepostiory permission_RoleRepostiory) {
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
        this.handlerMapping = handlerMapping;
        this.permission_RoleRepostiory = permission_RoleRepostiory;

    }

    private static final List<String> WHITE_LIST_PATTERNS = List.of(
            "/auth/**", "/error", "/api/permissions/**");

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @PostConstruct
    public void init() {
        // 1. Khởi tạo permission từ các route trong hệ thống
        initPermissions();

        // 2. Tạo role ADMIN nếu chưa có
        Role adminRole = roleRepository.findByRoleName("ADMIN").orElseGet(() -> {
            Role role = Role.builder()
                    .roleName("ADMIN")
                    .build();
            return roleRepository.save(role);
        });

        // 3. Gán tất cả permission cho ADMIN nếu chưa gán
        for (Permission permission : permissionRepository.findAll()) {
            permission_RoleRepostiory.save(Permission_Role.builder()
                    .permission(permission)
                    .role(adminRole)
                    .build());
        }
        // 4. Tạo account admin nếu chưa có
        if (!accountRepository.existsByRole(adminRole)) {
            Account account = Account.builder()
                    .email("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(adminRole)
                    .enabled(true)
                    .build();
            accountRepository.save(account);
        }
    }

    private void initPermissions() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            Set<String> urlPatterns = resolvePatterns(mappingInfo);
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();

            for (String url : urlPatterns) {
                if (isWhiteListed(url))
                    continue;

                for (RequestMethod method : methods) {
                    boolean exists = permissionRepository.existsByApiPathAndMethod(url, method.name());
                    if (!exists) {
                        permissionRepository.save(
                                Permission.builder()
                                        .name(generateName(url, method.name()))
                                        .apiPath(url)
                                        .method(method.name())
                                        .model(extractModule(url))
                                        .build());
                    }
                }
            }
        }
    }

    private Set<String> resolvePatterns(RequestMappingInfo info) {
        Set<String> patterns = new HashSet<>();
        if (info.getPatternsCondition() != null) {
            patterns.addAll(info.getPatternsCondition().getPatterns());
        } else if (info.getPathPatternsCondition() != null) {
            info.getPathPatternsCondition().getPatterns()
                    .forEach(p -> patterns.add(p.getPatternString()));
        }
        return patterns;
    }

    // Kiểm tra xem URL có thuộc white list không, hỗ trợ ** bằng AntPathMatcher
    private boolean isWhiteListed(String path) {
        return WHITE_LIST_PATTERNS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private String generateName(String path, String method) {
        return method + " " + path;
    }

    private String extractModule(String path) {
        String[] parts = path.split("/");
        for (String part : parts) {
            if (!part.isBlank() && !part.equalsIgnoreCase("api") && !part.matches("v[0-9]+")) {
                return capitalize(part);
            }
        }
        return "General";
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty())
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
