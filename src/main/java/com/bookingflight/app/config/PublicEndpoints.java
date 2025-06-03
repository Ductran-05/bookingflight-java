package com.bookingflight.app.config;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

public class PublicEndpoints {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static final List<String> ALL_METHODS = List.of(
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/confirm",
            "/api/auth/register",
            "/api/payment/vnpay-return");

    public static final List<String> GET_METHODS = List.of(
            "/api/airports/**",
            "/api/permissions/**",
            "/api/cities/**",
            "/api/airlines/**",
            "/api/flights/**",
            "/api/flights/seats/**",
            "/api/files/**",
            "/api/payment/**");

    public static boolean isPublic(String path, String method) {
        if (method.equalsIgnoreCase(HttpMethod.GET.name())) {
            return GET_METHODS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
        }
        return ALL_METHODS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
