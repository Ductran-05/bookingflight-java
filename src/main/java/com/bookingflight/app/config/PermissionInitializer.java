package com.bookingflight.app.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.bookingflight.app.domain.Permission;
import com.bookingflight.app.repository.PermissionRepository;

import java.util.*;

@Component
public class PermissionInitializer {

    private final RequestMappingHandlerMapping handlerMapping;
    private final PermissionRepository permissionRepository;

    public PermissionInitializer(
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
            PermissionRepository permissionRepository) {
        this.handlerMapping = handlerMapping;
        this.permissionRepository = permissionRepository;
    }

    // Danh sách các URL không cần phân quyền (bỏ qua)
    private static final List<String> WHITE_LIST_PREFIXES = List.of(
            "/auth", "/error");

    @PostConstruct
    public void initializePages() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            Set<String> urlPatterns = resolvePatterns(mappingInfo);
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();

            for (String url : urlPatterns) {
                // Bỏ qua URL trong danh sách trắng
                // if (isWhiteListed(url)) continue;

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

    private boolean isWhiteListed(String path) {
        return WHITE_LIST_PREFIXES.stream().anyMatch(path::startsWith);
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
