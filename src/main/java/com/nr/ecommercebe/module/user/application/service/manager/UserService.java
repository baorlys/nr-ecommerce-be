package com.nr.ecommercebe.module.user.application.service.manager;

import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponseDto> getAll(Pageable pageable);
    void deleteUser(String userId);
}
