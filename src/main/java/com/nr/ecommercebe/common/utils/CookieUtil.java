package com.nr.ecommercebe.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.Arrays;

public class CookieUtil {

    private static final String PATH = "/";
    private static final boolean HTTP_ONLY = true;
    private static final boolean SECURE = true;
    private static final String SAME_SITE = "Strict"; // or "Lax" depending on your needs
    private static final String ACCESS_TOKEN_NAME = "accessToken";
    private static final String REFRESH_TOKEN_NAME = "refreshToken";

    private CookieUtil() {
        // Prevent instantiation
    }

    public static ResponseCookie createAccessTokenCookie(String token, Duration duration) {
        return createCookie(ACCESS_TOKEN_NAME, token, duration);
    }

    public static ResponseCookie createRefreshTokenCookie(String token, Duration duration) {
        return createCookie(REFRESH_TOKEN_NAME, token, duration);
    }

    public static ResponseCookie clearAccessTokenCookie() {
        return clearCookie(ACCESS_TOKEN_NAME);
    }

    public static ResponseCookie clearRefreshTokenCookie() {
        return clearCookie(REFRESH_TOKEN_NAME);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private static ResponseCookie createCookie(String name, String value, Duration duration) {
        return ResponseCookie.from(name, value)
                .path(PATH)
                .httpOnly(HTTP_ONLY)
                .secure(SECURE)
                .sameSite(SAME_SITE)
                .maxAge(duration)
                .build();
    }

    private static ResponseCookie clearCookie(String name) {
        return ResponseCookie.from(name, "")
                .path(PATH)
                .httpOnly(HTTP_ONLY)
                .secure(SECURE)
                .sameSite(SAME_SITE)
                .maxAge(0)
                .build();
    }
}