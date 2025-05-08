package com.nr.ecommercebe.module.user.application.service.authentication;

import com.nr.ecommercebe.module.user.application.domain.Role;
import com.nr.ecommercebe.module.user.application.domain.RoleName;
import com.nr.ecommercebe.module.user.application.domain.TokenType;
import com.nr.ecommercebe.module.user.application.domain.User;
import com.nr.ecommercebe.module.user.application.dto.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.UpdateUserInfoRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.UpdateUserPasswordRequestDto;
import com.nr.ecommercebe.module.user.application.dto.response.LoginResponseDto;
import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import com.nr.ecommercebe.module.user.application.mapper.UserMapper;
import com.nr.ecommercebe.module.user.application.service.cache.RoleCacheService;
import com.nr.ecommercebe.module.user.infrastructure.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleCacheService roleCacheService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequestDto loginRequestDto;
    private RegisterRequestDto registerRequestDto;
    private UpdateUserInfoRequestDto updateUserInfoRequestDto;
    private UpdateUserPasswordRequestDto updateUserPasswordRequestDto;
    private User user;
    private UserResponseDto userResponseDto;
    private CustomUserDetails userDetails;
    private Role userRole;

    @BeforeEach
    void setUp() {
        loginRequestDto = LoginRequestDto.builder()
                .email("test@example.com")
                .password("password")
                .build();

        registerRequestDto = RegisterRequestDto.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();

        updateUserInfoRequestDto = UpdateUserInfoRequestDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .phone("0987654321")
                .build();

        updateUserPasswordRequestDto = UpdateUserPasswordRequestDto.builder()
                .currentPassword("password")
                .newPassword("newPassword")
                .build();

        userRole = new Role();
        userRole.setName(RoleName.USER);

        user = new User();
        user.setId("user1");
        user.setEmail("test@example.com");
        user.setPasswordHash("encodedPassword");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setRole(userRole);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId("user1");
        userResponseDto.setEmail("test@example.com");
        userResponseDto.setFirstName("John");
        userResponseDto.setLastName("Doe");
        userResponseDto.setPhone("1234567890");


        userDetails = new CustomUserDetails(user);
    }

    @Test
    void login_success_returnsLoginResponseDto() {
        // Arrange
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtService.generateAccessToken(userDetails)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refreshToken");
        when(mapper.toDTO(user)).thenReturn(userResponseDto);

        // Act
        LoginResponseDto result = authService.login(loginRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
        assertEquals(TokenType.BEARER, result.getTokenType());
        assertEquals(userResponseDto, result.getUser());
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateAccessToken(userDetails);
        verify(jwtService, times(1)).generateRefreshToken(userDetails);
        verify(mapper, times(1)).toDTO(user);
    }

    @Test
    void login_invalidCredentials_throwsBadCredentialsException() {
        // Arrange
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authService.login(loginRequestDto));
        assertEquals("Invalid credentials", exception.getMessage());
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtService, mapper);
    }

    @Test
    void register_success_returnsUserId() {
        // Arrange
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(mapper.toEntity(registerRequestDto)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleCacheService.getRole(RoleName.USER)).thenReturn(userRole);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        String userId = authService.register(registerRequestDto);

        // Assert
        assertEquals("user1", userId);
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(mapper, times(1)).toEntity(registerRequestDto);
        verify(passwordEncoder, times(1)).encode("password");
        verify(roleCacheService, times(1)).getRole(RoleName.USER);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_emailExists_throwsRuntimeException() {
        // Arrange
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequestDto));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(mapper, passwordEncoder, roleCacheService);
    }
    @Test
    void refreshToken_success_returnsAccessToken() {
        // Arrange
        when(jwtService.isTokenValid("refreshToken")).thenReturn(true);
        when(jwtService.getUsername("refreshToken")).thenReturn("user1");
        when(jwtService.getRole("refreshToken")).thenReturn("ROLE_USER");
        when(roleCacheService.getRole(RoleName.USER)).thenReturn(userRole);
        when(jwtService.generateAccessToken(any(CustomUserDetails.class))).thenReturn("newAccessToken");

        // Act
        String accessToken = authService.refreshToken("refreshToken");

        // Assert
        assertEquals("newAccessToken", accessToken);
        verify(jwtService, times(1)).isTokenValid("refreshToken");
        verify(jwtService, times(1)).getUsername("refreshToken");
        verify(jwtService, times(1)).getRole("refreshToken");
        verify(roleCacheService, times(1)).getRole(RoleName.USER);
        verify(jwtService, times(1)).generateAccessToken(any(CustomUserDetails.class));
    }

    @Test
    void refreshToken_invalidToken_throwsJwtException() {
        // Arrange
        when(jwtService.isTokenValid("refreshToken")).thenReturn(false);

        // Act & Assert
        JwtException exception = assertThrows(JwtException.class,
                () -> authService.refreshToken("refreshToken"));
        assertEquals("JWT is invalid", exception.getMessage());
        verify(jwtService, times(1)).isTokenValid("refreshToken");
        verifyNoMoreInteractions(jwtService, roleCacheService);
    }

    @Test
    void logout_noOp_doesNothing() {
        // Act
        authService.logout("token");

        // Assert
        verifyNoInteractions(userRepository, jwtService, mapper, roleCacheService, authManager, passwordEncoder);
    }

    @Test
    void getCurrentUser_success_returnsUserResponseDto() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(true);
        when(jwtService.getUsername("accessToken")).thenReturn("user1");
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(mapper.toDTO(user)).thenReturn(userResponseDto);

        // Act
        UserResponseDto result = authService.getCurrentUser("accessToken");

        // Assert
        assertNotNull(result);
        assertEquals("user1", result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verify(jwtService, times(1)).getUsername("accessToken");
        verify(userRepository, times(1)).findById("user1");
        verify(mapper, times(1)).toDTO(user);
    }

    @Test
    void getCurrentUser_invalidToken_throwsJwtException() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(false);

        // Act & Assert
        JwtException exception = assertThrows(JwtException.class,
                () -> authService.getCurrentUser("accessToken"));
        assertEquals("JWT is invalid", exception.getMessage());
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verifyNoInteractions(userRepository, mapper);
    }

    @Test
    void updateUser_success_returnsUserResponseDto() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(true);
        when(jwtService.getUsername("accessToken")).thenReturn("user1");
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(userResponseDto);

        // Act
        UserResponseDto result = authService.updateUser(updateUserInfoRequestDto, "accessToken");

        // Assert
        assertNotNull(result);
        assertEquals("user1", result.getId());
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verify(jwtService, times(1)).getUsername("accessToken");
        verify(userRepository, times(1)).findById("user1");
        verify(userRepository, times(1)).save(any(User.class));
        verify(mapper, times(1)).toDTO(user);
    }

    @Test
    void updateUser_invalidToken_throwsJwtException() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(false);

        // Act & Assert
        JwtException exception = assertThrows(JwtException.class,
                () -> authService.updateUser(updateUserInfoRequestDto, "accessToken"));
        assertEquals("JWT is invalid", exception.getMessage());
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verifyNoInteractions(userRepository, mapper);
    }

    @Test
    void updatePassword_success_updatesPassword() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(true);
        when(jwtService.getUsername("accessToken")).thenReturn("user1");
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        authService.updatePassword(updateUserPasswordRequestDto, "accessToken");

        // Assert
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verify(jwtService, times(1)).getUsername("accessToken");
        verify(userRepository, times(1)).findById("user1");
        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updatePassword_incorrectCurrentPassword_throwsBadCredentialsException() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(true);
        when(jwtService.getUsername("accessToken")).thenReturn("user1");
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authService.updatePassword(updateUserPasswordRequestDto, "accessToken"));
        assertEquals("Password is incorrect", exception.getMessage());
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verify(jwtService, times(1)).getUsername("accessToken");
        verify(userRepository, times(1)).findById("user1");
        verify(passwordEncoder, times(1)).matches("password", "encodedPassword");
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }

    @Test
    void getUserFromAccessToken_success_returnsUser() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(true);
        when(jwtService.getUsername("accessToken")).thenReturn("user1");
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));

        // Act
        User result = authService.getUserFromAccessToken("accessToken");

        // Assert
        assertNotNull(result);
        assertEquals("user1", result.getId());
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verify(jwtService, times(1)).getUsername("accessToken");
        verify(userRepository, times(1)).findById("user1");
    }

    @Test
    void getUserFromAccessToken_userNotFound_throwsJwtException() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(true);
        when(jwtService.getUsername("accessToken")).thenReturn("user1");
        when(userRepository.findById("user1")).thenReturn(Optional.empty());

        // Act & Assert
        JwtException exception = assertThrows(JwtException.class,
                () -> authService.getUserFromAccessToken("accessToken"));
        assertEquals("User not found", exception.getMessage());
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verify(jwtService, times(1)).getUsername("accessToken");
        verify(userRepository, times(1)).findById("user1");
    }

    @Test
    void getUserIdFromAccessToken_success_returnsUserId() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(true);
        when(jwtService.getUsername("accessToken")).thenReturn("user1");

        // Act
        String userId = authService.getUserIdFromToken("accessToken");

        // Assert
        assertEquals("user1", userId);
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verify(jwtService, times(1)).getUsername("accessToken");
    }

    @Test
    void getUserIdFromToken_invalidToken_throwsJwtException() {
        // Arrange
        when(jwtService.isTokenValid("accessToken")).thenReturn(false);

        // Act & Assert
        JwtException exception = assertThrows(JwtException.class,
                () -> authService.getUserIdFromToken("accessToken"));
        assertEquals("JWT is invalid", exception.getMessage());
        verify(jwtService, times(1)).isTokenValid("accessToken");
        verifyNoMoreInteractions(jwtService);
    }
}