package com.nr.ecommercebe.module.user.application.service.authentication;

import com.nr.ecommercebe.module.user.application.dto.request.UpdateUserInfoRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.UpdateUserPasswordRequestDto;
import com.nr.ecommercebe.module.user.application.mapper.UserMapper;
import com.nr.ecommercebe.module.user.application.service.cache.RoleCacheService;
import com.nr.ecommercebe.module.user.application.service.cache.UserCacheService;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.service.CommonExceptionService;
import com.nr.ecommercebe.module.user.application.dto.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.application.dto.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.application.dto.response.LoginResponseDto;
import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import com.nr.ecommercebe.module.user.application.domain.Role;
import com.nr.ecommercebe.module.user.application.domain.RoleName;
import com.nr.ecommercebe.module.user.application.domain.TokenType;
import com.nr.ecommercebe.module.user.application.domain.User;
import com.nr.ecommercebe.module.user.infrastructure.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;

    RoleCacheService roleCacheService;
    UserCacheService userCacheService;
    AuthenticationManager authManager;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;

    UserMapper mapper;


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        UserResponseDto userResponse = mapper.toDTO(userDetails.user());
        userCacheService.cacheUser(userResponse);

        log.info("User {} logged in at {}", userResponse.getId(), LocalDateTime.now());

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .user(userResponse)
                .build();
    }

    @Override
    public String register(RegisterRequestDto registerRequestDto) {
        Boolean isEmailExist = userRepository.existsByEmail(registerRequestDto.getEmail());
        CommonExceptionService.throwRecordExists(isEmailExist, ErrorCode.EMAIL_EXISTS.getMessage());

        User user = mapper.toEntity(registerRequestDto);
        user.setPasswordHash(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(roleCacheService.getRole(RoleName.USER));

        User savedUser = userRepository.save(user);
        log.info("User {} created at {}", savedUser.getId(), LocalDateTime.now());

        return savedUser.getId();
    }

    @Override
    public String refreshToken(String refreshToken) {
        if(!jwtService.isTokenValid(refreshToken)) {
            throw new JwtException(ErrorCode.JWT_IS_INVALID.getMessage());
        }

        String userId = jwtService.getUsername(refreshToken);
        String roleName = jwtService.getRole(refreshToken).replace("ROLE_", "");
        Role role = roleCacheService.getRole(RoleName.valueOf(roleName));

        User user = new User();
        user.setId(userId);
        user.setRole(role);

        return jwtService.generateAccessToken(new CustomUserDetails(user));
    }

    @Override
    public void logout(String refreshToken) {
        String userId = getUserIdFromToken(refreshToken);
        userCacheService.evictUserById(userId);
        log.info("User {} logged out at {}", userId, LocalDateTime.now());
    }

    @Override
    public UserResponseDto getCurrentUser(String accessToken) {
        String userId = getUserIdFromToken(accessToken);

        UserResponseDto userInCache = userCacheService.getCachedUserById(userId);
        if (userInCache != null) {
            return userInCache;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new JwtException(ErrorCode.USER_NOT_FOUND.getMessage()));
        UserResponseDto userResponse = mapper.toDTO(user);
        userCacheService.cacheUser(userResponse);
        return userResponse;
    }

    @Override
    public UserResponseDto updateUser(UpdateUserInfoRequestDto updateUserInfoRequestDto, String accessToken) {
        User user = getUserFromAccessToken(accessToken);

        userCacheService.evictUserById(user.getId());

        user.setFirstName(updateUserInfoRequestDto.getFirstName());
        user.setLastName(updateUserInfoRequestDto.getLastName());
        user.setPhone(updateUserInfoRequestDto.getPhone());

        userRepository.save(user);
        log.info("User {} updated at {}", user.getId(), LocalDateTime.now());
        UserResponseDto userResponse = mapper.toDTO(user);

        userCacheService.cacheUser(userResponse);
        return userResponse;
    }

    @Override
    public void updatePassword(UpdateUserPasswordRequestDto updateUserPasswordRequestDto, String accessToken) {
        User user = getUserFromAccessToken(accessToken);

        String currentPassword = user.getPasswordHash();
        String currentPasswordRequest = updateUserPasswordRequestDto.getCurrentPassword();
        String newPassword = updateUserPasswordRequestDto.getNewPassword();

        if (!passwordEncoder.matches(currentPasswordRequest, currentPassword)) {
            throw new BadCredentialsException(ErrorCode.PASSWORD_IS_INCORRECT.getMessage());
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("User {} updated password at {}", user.getId(), LocalDateTime.now());
    }

    @Override
    public User getUserFromAccessToken(String accessToken) {
        String userId = getUserIdFromToken(accessToken);
        return userRepository.findById(userId)
                .orElseThrow(() -> new JwtException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Override
    public String getUserIdFromToken(String token) {
        if(!jwtService.isTokenValid(token)) {
            throw new JwtException(ErrorCode.JWT_IS_INVALID.getMessage());
        }

        return jwtService.getUsername(token);
    }
}
