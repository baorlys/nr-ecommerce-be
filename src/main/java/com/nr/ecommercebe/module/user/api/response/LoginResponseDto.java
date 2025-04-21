package com.nr.ecommercebe.module.user.api.response;

import com.nr.ecommercebe.module.user.model.TokenType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    String accessToken;
    String refreshToken;
    TokenType tokenType;
    UserResponseDto user;
}
