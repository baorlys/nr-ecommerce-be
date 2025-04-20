package com.nr.ecommercebe.module.user.api;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    String register(RegisterRequestDto registerRequestDto);

    String refreshToken(String refreshToken);

    void logout(String token);
}
