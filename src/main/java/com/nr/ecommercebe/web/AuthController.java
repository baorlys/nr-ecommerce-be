package com.nr.ecommercebe.web;

import com.nr.ecommercebe.common.utils.CookieUtil;
import com.nr.ecommercebe.module.user.api.*;
import com.nr.ecommercebe.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return ResponseEntity.ok(loginResponse.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid String token) {
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody @Valid String refreshToken) {
        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }



}
