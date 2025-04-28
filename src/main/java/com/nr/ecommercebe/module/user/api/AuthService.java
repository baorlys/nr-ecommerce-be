package com.nr.ecommercebe.module.user.api;

import com.nr.ecommercebe.module.user.api.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.api.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.api.response.LoginResponseDto;
import com.nr.ecommercebe.module.user.api.response.UserResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    String register(RegisterRequestDto registerRequestDto);

    String refreshToken(String refreshToken);

    void logout(String token);

    UserResponseDto getCurrentUser(String accessToken);
}
