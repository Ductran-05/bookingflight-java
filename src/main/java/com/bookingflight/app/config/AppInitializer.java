package com.bookingflight.app.config;

import jakarta.annotation.PostConstruct;
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
            "/api/auth/**", "/error", "/api/permissions/**");

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @PostConstruct
    public void init() {
        initializePermissions();
        initializeAdmin();
    }

    private void initializePermissions() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();

            Set<String> urlPatterns = resolvePatterns(mappingInfo);
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
            for (String rawUrl : urlPatterns) {
                String url = normalizePath(rawUrl);
                if (isWhiteListed(url))
                    continue;
                for (RequestMethod method : methods) {
                    // Nếu đã tồn tại thì bỏ qua
                    if (permissionRepository.existsByApiPathAndMethod(url, method.name()))
                        continue;
                    Permission permission = Permission.builder()
                            .apiPath(url)
                            .method(method.name())
                            .name(generateName(url, method.name()))
                            .model(extractModel(url))
                            .build();
                    permissionRepository.save(permission);
                }
            }
        }
    }

    private void initializeAdmin() {
        Role adminRole = roleRepository.findByRoleName("ADMIN").orElseGet(() -> {
            Role newRole = Role.builder()
                    .roleName("ADMIN")
                    .build();
            return roleRepository.save(newRole);
        });
        // gán quyen cho admin
        List<Permission> permissions = permissionRepository.findAll();
        for (Permission permission : permissions) {
            Permission_Role permission_Role = Permission_Role.builder()
                    .permission(permission)
                    .role(adminRole)
                    .build();
            permission_RoleRepostiory.save(permission_Role);
        }
        // tạo tài khoản admin
        boolean existsAdmin = accountRepository.existsByRole(adminRole);
        if (!existsAdmin) {
            Account account = Account.builder()
                    .email("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(adminRole)
                    .enabled(true)
                    .build();
            accountRepository.save(account);
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

    private String normalizePath(String path) {
        // Thay {id}, {abc}... bằng **
        path = path.replaceAll("\\{[^/]+}", "**");

        // Nếu path không kết thúc bằng '/**' và không có phần mở rộng sau tiền tố (ví
        // dụ: /accounts), thêm '/**'
        if (!path.endsWith("/**")) {
            path = path.replaceAll("/$", ""); // xóa dấu "/" cuối nếu có
            path += "/**";
        }

        return path;
    }

    private boolean isWhiteListed(String path) {
        return WHITE_LIST_PATTERNS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private String generateName(String path, String method) {
        return method + " " + path;
    }

    private String extractModel(String path) {
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
