package com.nr.ecommercebe.module.user.application.service.manager;

import com.nr.ecommercebe.module.user.application.domain.User;
import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import com.nr.ecommercebe.module.user.application.service.cache.RoleCacheService;
import com.nr.ecommercebe.module.user.application.mapper.UserMapper;
import com.nr.ecommercebe.module.user.infrastructure.repository.UserRepository;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleCacheService roleCacheService;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponseDto userResponseDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user1");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setDeleted(false);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId("user1");
        userResponseDto.setEmail("test@example.com");
        userResponseDto.setFirstName("John");
        userResponseDto.setLastName("Doe");
        userResponseDto.setPhone("1234567890");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getAll_success_returnsPagedUsers() {
        // Arrange
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(mapper.toDTO(user)).thenReturn(userResponseDto);

        // Act
        Page<UserResponseDto> result = userService.getAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("user1", result.getContent().get(0).getId());
        assertEquals("test@example.com", result.getContent().get(0).getEmail());
        verify(userRepository, times(1)).findAll(pageable);
        verify(mapper, times(1)).toDTO(user);
    }

    @Test
    void getAll_emptyPage_returnsEmpty() {
        // Arrange
        Page<User> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(userRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<UserResponseDto> result = userService.getAll(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(userRepository, times(1)).findAll(pageable);
        verifyNoInteractions(mapper);
    }

    @Test
    void deleteUser_success_marksUserAsDeleted() {
        // Arrange
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.deleteUser("user1");

        // Assert
        assertTrue(user.isDeleted());
        verify(userRepository, times(1)).findById("user1");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_userNotFound_throwsRecordNotFoundException() {
        // Arrange
        when(userRepository.findById("user1")).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> userService.deleteUser("user1"));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById("user1");
        verify(userRepository, never()).save(any());
    }
}