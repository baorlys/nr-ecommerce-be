package com.nr.ecommercebe.module.user.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    String accessToken;
    String refreshToken;
    String tokenType;
    UserResponseDto user;
}
