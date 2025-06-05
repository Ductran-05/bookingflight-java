package com.bookingflight.app.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
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
    private final Permission_RoleRepostiory permissionRoleRepository;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AppInitializer(RoleRepository roleRepository, AccountRepository accountRepository,
            PasswordEncoder passwordEncoder, PermissionRepository permissionRepository,
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
            Permission_RoleRepostiory permissionRoleRepository) {
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
        this.handlerMapping = handlerMapping;
        this.permissionRoleRepository = permissionRoleRepository;
    }

    @PostConstruct
    public void init() {
        initializePermissions();
        initializeAdmin();
        initializeUser();
    }

    /**
     * Tạo mới các quyền (Permission) dựa trên các endpoint đã khai báo trong
     * controller,
     * bỏ qua các endpoint public theo định nghĩa ở PublicEndpoints.
     */
    private void initializePermissions() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (var entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            Set<String> urlPatterns = resolvePatterns(mappingInfo);
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();

            for (String rawUrl : urlPatterns) {
                for (RequestMethod method : methods) {
                    String methodName = method.name();

                    // Bỏ qua các endpoint public không cần tạo quyền
                    if (PublicEndpoints.isPublic(rawUrl, HttpMethod.valueOf(methodName)))
                        continue;

                    String normalizedUrl = normalizePath(rawUrl);

                    if (permissionRepository.existsByApiPathAndMethod(normalizedUrl, methodName))
                        continue;

                    Permission permission = Permission.builder()
                            .apiPath(normalizedUrl)
                            .method(methodName)
                            .name(generatePermissionName(extractModelFromPath(normalizedUrl), methodName))
                            .model(extractModelFromPath(normalizedUrl))
                            .build();

                    permissionRepository.save(permission);
                }
            }
        }
    }

    /**
     * Tạo role ADMIN nếu chưa có, gán tất cả quyền cho ADMIN,
     * và tạo tài khoản admin mặc định nếu chưa tồn tại.
     */
    private void initializeAdmin() {
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().roleName("ADMIN").build()));

        // Gán tất cả quyền cho ADMIN
        List<Permission> allPermissions = permissionRepository.findAll();
        for (Permission permission : allPermissions) {
            // Bỏ qua nếu đã tồn tại quan hệ
            if (permissionRoleRepository.existsByPermissionAndRole(permission, adminRole))
                continue;
            Permission_Role permissionRole = Permission_Role.builder()
                    .permission(permission)
                    .role(adminRole)
                    .build();
            permissionRoleRepository.save(permissionRole);
        }

        // Tạo tài khoản admin mặc định nếu chưa có
        boolean existsAdmin = accountRepository.existsByRole(adminRole);
        if (!existsAdmin) {
            Account adminAccount = Account.builder()
                    .email("admin")
                    .password(passwordEncoder.encode("admin"))
                    .fullName("Admin")
                    .role(adminRole)
                    .enabled(true)
                    .build();
            accountRepository.save(adminAccount);
        }
    }

    /**
     * Tạo role user mặc định nếu chưa có, gán quyền về /my-profile cho
     * role user
     */
    private void initializeUser() {
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .roleName("USER")
                        .build()));

        // Gán các quyền antMatcher /my-profile/** cho role user nếu chưa có
        for (Permission permission : permissionRepository.findAll()) {
            if (antPathMatcher.match("/api/my-profile/**", permission.getApiPath())
                    || antPathMatcher.match("/api/booking-flight/**", permission.getApiPath())) {
                // Bỏ qua nếu đã tồn tại quan hệ
                if (permissionRoleRepository.existsByPermissionAndRole(permission, userRole))
                    continue;
                Permission_Role permissionRole = Permission_Role.builder()
                        .permission(permission)
                        .role(userRole)
                        .build();
                permissionRoleRepository.save(permissionRole);
            }
        }
    }
    // ------------------- Hỗ trợ các hàm riêng -------------------

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

    /**
     * Chuẩn hóa đường dẫn:
     * - Thay các tham số path {id} thành **
     * - Nếu chưa kết thúc bằng /** thì thêm vào để đại diện cho mọi sub-path
     */
    private String normalizePath(String path) {
        path = path.replaceAll("\\{[^/]+}", "**");
        if (!path.endsWith("/**")) {
            path = path.replaceAll("/$", "");
            path += "/**";
        }
        return path;
    }

    private String generatePermissionName(String model, String method) {
        return method + "_" + model;
    }

    /**
     * Trích xuất model từ path, ví dụ "/api/accounts/**" => "Accounts"
     */
    private String extractModelFromPath(String path) {
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
        // return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        return s.substring(0).toUpperCase();
    }
}
