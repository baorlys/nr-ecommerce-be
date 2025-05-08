package com.nr.ecommercebe.module.user.web;

import com.nr.ecommercebe.module.user.application.dto.request.UpdateUserInfoRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.UpdateUserPasswordRequestDto;
import com.nr.ecommercebe.module.user.application.service.authentication.AuthService;
import com.nr.ecommercebe.shared.util.CookieUtil;
import com.nr.ecommercebe.module.user.application.dto.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.application.dto.response.LoginResponseDto;
import com.nr.ecommercebe.module.user.application.dto.response.RegisterResponseDto;
import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import com.nr.ecommercebe.module.user.application.service.authentication.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Authentication endpoints for user registration, login, and account management")
public class AuthController {

    AuthService authService;
    JwtService jwtService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid input")
            }
    )
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
    @Operation(
            summary = "Login a user",
            description = "Authenticates the user and returns the user details along with access and refresh tokens in cookies",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials")
            }
    )
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
    @Operation(
            summary = "Get current user details",
            description = "Fetches the details of the currently authenticated user based on the access token stored in cookies",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched user details"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<UserResponseDto> getCurrentUser(HttpServletRequest request) {
        String accessToken = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);
        UserResponseDto user = authService.getCurrentUser(accessToken);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    @Operation(
            summary = "Update current user's information",
            description = "Allows the currently authenticated user to update their information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<UserResponseDto> updateUser(
            @RequestBody @Valid UpdateUserInfoRequestDto updateUserInfoRequestDto,
            HttpServletRequest request
    ) {
        String accessToken = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);
        UserResponseDto updatedUser = authService.updateUser(updateUserInfoRequestDto, accessToken);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/me/password")
    @Operation(
            summary = "Update current user's password",
            description = "Allows the currently authenticated user to change their password",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Password updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid password or data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Void> updatePassword(
            @RequestBody @Valid UpdateUserPasswordRequestDto updateUserPasswordRequestDto,
            HttpServletRequest request
    ) {
        String accessToken = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);
        authService.updatePassword(updateUserPasswordRequestDto, accessToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout the current user",
            description = "Logs out the user by invalidating their refresh token and clearing cookies",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User logged out successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid token")
            }
    )
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, CookieUtil.REFRESH_TOKEN_NAME);
        authService.logout(refreshToken);
        ResponseCookie clearAccessTokenCookie = CookieUtil.clearAccessTokenCookie();
        ResponseCookie clearRefreshTokenCookie = CookieUtil.clearRefreshTokenCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, clearAccessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefreshTokenCookie.toString());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh access token",
            description = "Refreshes the access token using the provided refresh token",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Access token refreshed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid refresh token")
            }
    )
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
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
