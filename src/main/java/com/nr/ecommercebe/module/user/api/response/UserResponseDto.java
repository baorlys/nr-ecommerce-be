package com.nr.ecommercebe.module.user.api.response;

import com.nr.ecommercebe.module.user.model.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    String id;
    String email;
    String firstName;
    String lastName;
    String phone;
    String fullName;
    RoleName role;
}

