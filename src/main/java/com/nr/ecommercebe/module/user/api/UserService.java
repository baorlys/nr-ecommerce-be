package com.nr.ecommercebe.module.user.api;

import com.nr.ecommercebe.module.user.api.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponseDto> getAll(Pageable pageable);
    void deleteUser(String userId);
}
