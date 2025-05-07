package com.nr.ecommercebe.module.user.application.service.authentication;

import com.nr.ecommercebe.module.user.application.dto.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.UpdateUserInfoRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.UpdateUserPasswordRequestDto;
import com.nr.ecommercebe.module.user.application.dto.response.LoginResponseDto;
import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import com.nr.ecommercebe.module.user.application.domain.User;

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
