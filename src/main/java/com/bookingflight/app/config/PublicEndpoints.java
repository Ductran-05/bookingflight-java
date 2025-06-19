package com.bookingflight.app.config;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

public class PublicEndpoints {

        private static final AntPathMatcher pathMatcher = new AntPathMatcher();

        public static final List<String> ALL_METHODS = List.of(
                        "/api/refresh/**",
                        "/api/auth/**",
                        "/api/dashboard/**",
                        "/api/payment/**",
                        "/api/booking-flight/**",
                        "/api/payment/**",
                        "/api/my-profile/**",
                        "/api/airlines/flights/airline-popular/**",
                        "/login/oauth2/**",
                        "/oauth2/**",
                        "/api/files/**",
                        "/api/auth/forgot-password/**",
                        "/api/auth/reset-password/**",
                        "/api/tickets/pickup/**");

        public static final List<String> GET_METHODS = List.of(
                        "/api/airports/**",
                        "/api/cities/**",
                        "/api/permissions/**",
                        "/api/airlines/**",
                        "/api/flights/**",
                        "/api/flights/seats/**",
                        "/api/tickets/**",
                        "/api/seats/**",
                        "/api/files/**");

        /* 
        
        */

        public static boolean isPublic(String path, HttpMethod method) {
                // Check ALL_METHODS first
                for (String publicPath : ALL_METHODS) {
                        if (pathMatcher.match(publicPath, path)) {
                                return true;
                        }
                }

                // Check GET_METHODS for GET requests
                if (HttpMethod.GET.equals(method)) {
                        for (String publicPath : GET_METHODS) {
                                if (pathMatcher.match(publicPath, path)) {
                                        return true;
                                }
                        }
                }

                return false;
        }

        public static boolean isPublicAllMethods(String rawUrl) {
                for (String publicPath : ALL_METHODS) {
                        if (pathMatcher.match(publicPath, rawUrl)) {
                                return true;
                        }
                }
                return false;
        }
}
