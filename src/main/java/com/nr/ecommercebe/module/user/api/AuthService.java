package com.nr.ecommercebe.module.user.api;

import com.nr.ecommercebe.module.user.api.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.api.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.api.response.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    String register(RegisterRequestDto registerRequestDto);

    String refreshToken(String refreshToken);

    void logout(String token);
}
