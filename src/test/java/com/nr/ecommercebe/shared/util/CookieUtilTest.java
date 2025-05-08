package com.nr.ecommercebe.shared.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieUtilTest {

    @Mock
    private HttpServletRequest request;

    @Test
    void createAccessTokenCookie_success_createsCookieWithCorrectProperties() {
        // Arrange
        String token = "accessToken123";
        Duration duration = Duration.ofHours(1);

        // Act
        ResponseCookie cookie = CookieUtil.createAccessTokenCookie(token, duration);

        // Assert
        assertNotNull(cookie);
        assertEquals(CookieUtil.ACCESS_TOKEN_NAME, cookie.getName());
        assertEquals(token, cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("Strict", cookie.getSameSite());
        assertEquals(duration.getSeconds(), cookie.getMaxAge().getSeconds());
    }

    @Test
    void createRefreshTokenCookie_success_createsCookieWithCorrectProperties() {
        // Arrange
        String token = "refreshToken123";
        Duration duration = Duration.ofDays(7);

        // Act
        ResponseCookie cookie = CookieUtil.createRefreshTokenCookie(token, duration);

        // Assert
        assertNotNull(cookie);
        assertEquals(CookieUtil.REFRESH_TOKEN_NAME, cookie.getName());
        assertEquals(token, cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("Strict", cookie.getSameSite());
        assertEquals(duration.getSeconds(), cookie.getMaxAge().getSeconds());
    }

    @Test
    void clearAccessTokenCookie_success_createsClearedCookie() {
        // Act
        ResponseCookie cookie = CookieUtil.clearAccessTokenCookie();

        // Assert
        assertNotNull(cookie);
        assertEquals(CookieUtil.ACCESS_TOKEN_NAME, cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("Strict", cookie.getSameSite());
        assertEquals(0, cookie.getMaxAge().getSeconds());
    }

    @Test
    void clearRefreshTokenCookie_success_createsClearedCookie() {
        // Act
        ResponseCookie cookie = CookieUtil.clearRefreshTokenCookie();

        // Assert
        assertNotNull(cookie);
        assertEquals(CookieUtil.REFRESH_TOKEN_NAME, cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("Strict", cookie.getSameSite());
        assertEquals(0, cookie.getMaxAge().getSeconds());
    }

    @Test
    void getCookieValue_cookieExists_returnsCookieValue() {
        // Arrange
        Cookie cookie = new Cookie(CookieUtil.ACCESS_TOKEN_NAME, "accessToken123");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        // Act
        String value = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);

        // Assert
        assertEquals("accessToken123", value);
        verify(request, times(1)).getCookies();
    }

    @Test
    void getCookieValue_noCookies_returnsNull() {
        // Arrange
        when(request.getCookies()).thenReturn(null);

        // Act
        String value = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);

        // Assert
        assertNull(value);
        verify(request, times(1)).getCookies();
    }

    @Test
    void getCookieValue_cookieNotFound_returnsNull() {
        // Arrange
        Cookie cookie = new Cookie("otherCookie", "otherValue");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        // Act
        String value = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);

        // Assert
        assertNull(value);
        verify(request, times(1)).getCookies();
    }
}