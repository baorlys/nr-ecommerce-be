package com.nr.ecommercebe.module.user.api;

import com.nr.ecommercebe.module.user.api.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.api.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.api.request.UpdateUserInfoRequestDto;
import com.nr.ecommercebe.module.user.api.request.UpdateUserPasswordRequestDto;
import com.nr.ecommercebe.module.user.api.response.LoginResponseDto;
import com.nr.ecommercebe.module.user.api.response.UserResponseDto;
import com.nr.ecommercebe.module.user.model.User;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    String register(RegisterRequestDto registerRequestDto);

    String refreshToken(String refreshToken);

    void logout(String token);

    UserResponseDto getCurrentUser(String accessToken);

    UserResponseDto updateUser(UpdateUserInfoRequestDto updateUserInfoRequestDto, String accessToken);

    void updatePassword(UpdateUserPasswordRequestDto updateUserPasswordRequestDto, String accessToken);

    User getUserFromAccessToken(String accessToken);

    String getUserIdFromAccessToken(String accessToken);
}
