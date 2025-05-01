package com.nr.ecommercebe.web;

import com.nr.ecommercebe.shared.util.CookieUtil;
import com.nr.ecommercebe.module.user.api.*;
import com.nr.ecommercebe.module.user.api.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.api.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.api.response.LoginResponseDto;
import com.nr.ecommercebe.module.user.api.response.RegisterResponseDto;
import com.nr.ecommercebe.module.user.api.response.UserResponseDto;
import com.nr.ecommercebe.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {
    AuthService authService;
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        String createdUserId = authService.register(registerRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("api/v1/user/{userId}")
                .buildAndExpand(createdUserId)
                .toUri();

        RegisterResponseDto responseDto = RegisterResponseDto.builder()
                .message("User registered successfully")
                .userId(createdUserId)
                .location(location.getPath())
                .build();

        return ResponseEntity.created(location).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                                 HttpServletResponse response) {
        LoginResponseDto loginResponse = authService.login(loginRequestDto);


        ResponseCookie accessTokenCookie = CookieUtil.createAccessTokenCookie(
                loginResponse.getAccessToken(),
                Duration.ofMillis(jwtService.getAccessExpiration())
        );
        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(
                loginResponse.getRefreshToken(),
                Duration.ofMillis(jwtService.getRefreshExpiration())
        );

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok(loginResponse.getUser());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(
            HttpServletRequest request
    ) {
        String accessToken = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);
        UserResponseDto user = authService.getCurrentUser(accessToken);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = CookieUtil.getCookieValue(request, CookieUtil.REFRESH_TOKEN_NAME);
        authService.logout(refreshToken);
        ResponseCookie clearAccessTokenCookie = CookieUtil.clearAccessTokenCookie();
        ResponseCookie clearRefreshTokenCookie = CookieUtil.clearRefreshTokenCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, clearAccessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefreshTokenCookie.toString());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request,
                                             HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, CookieUtil.REFRESH_TOKEN_NAME);
        String newAccessToken = authService.refreshToken(refreshToken);

        ResponseCookie accessTokenCookie = CookieUtil.createAccessTokenCookie(
                newAccessToken,
                Duration.ofMillis(jwtService.getAccessExpiration())
        );

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        return ResponseEntity.noContent().build();
    }



}
