package com.nr.ecommercebe.module.user.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterResponseDto {
    String message;
    String userId;
    String location;
}
