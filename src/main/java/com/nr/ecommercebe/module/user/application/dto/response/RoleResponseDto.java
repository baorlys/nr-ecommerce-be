package com.nr.ecommercebe.module.user.application.dto.response;

import com.nr.ecommercebe.module.user.application.domain.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {
    String id;
    RoleName name;
}
