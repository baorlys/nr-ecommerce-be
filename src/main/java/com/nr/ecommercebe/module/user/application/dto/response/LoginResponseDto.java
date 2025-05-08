package com.nr.ecommercebe.module.user.application.dto.response;

import com.nr.ecommercebe.module.user.application.domain.TokenType;
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
