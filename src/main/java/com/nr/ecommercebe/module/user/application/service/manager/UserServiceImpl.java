package com.nr.ecommercebe.module.user.application.service.manager;

import com.nr.ecommercebe.module.user.application.mapper.UserMapper;
import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import com.nr.ecommercebe.module.user.application.initializer.RoleCache;
import com.nr.ecommercebe.module.user.application.domain.User;
import com.nr.ecommercebe.module.user.infrastructure.repository.UserRepository;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleCache roleCache;
    UserMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(mapper::toDTO);
    }


    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
        user.setDeleted(true);
        userRepository.save(user);
    }
}
