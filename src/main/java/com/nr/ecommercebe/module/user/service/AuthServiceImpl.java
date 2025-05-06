package com.nr.ecommercebe.module.user.service;

import com.nr.ecommercebe.module.user.api.request.UpdateUserInfoRequestDto;
import com.nr.ecommercebe.module.user.api.request.UpdateUserPasswordRequestDto;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.service.CommonExceptionService;
import com.nr.ecommercebe.module.user.api.*;
import com.nr.ecommercebe.module.user.api.request.LoginRequestDto;
import com.nr.ecommercebe.module.user.api.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.api.response.LoginResponseDto;
import com.nr.ecommercebe.module.user.api.response.UserResponseDto;
import com.nr.ecommercebe.module.user.initializer.RoleCache;
import com.nr.ecommercebe.module.user.model.Role;
import com.nr.ecommercebe.module.user.model.RoleName;
import com.nr.ecommercebe.module.user.model.TokenType;
import com.nr.ecommercebe.module.user.model.User;
import com.nr.ecommercebe.module.user.repository.UserRepository;
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

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;

    RoleCache roleCache;
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
        user.setRole(roleCache.getUserRole());

        return userRepository.save(user).getId();
    }

    @Override
    public String refreshToken(String refreshToken) {
        if(!jwtService.isTokenValid(refreshToken)) {
            throw new JwtException(ErrorCode.JWT_IS_INVALID.getMessage());
        }

        String userId = jwtService.getUsername(refreshToken);
        String roleName = jwtService.getRole(refreshToken).replace("ROLE_", "");
        Role role = roleCache.getRoleByName(RoleName.valueOf(roleName));

        User user = new User();
        user.setId(userId);
        user.setRole(role);

        return jwtService.generateAccessToken(new CustomUserDetails(user));
    }

    @Override
    public void logout(String token) {
        // Implement blacklist token logic
    }

    @Override
    public UserResponseDto getCurrentUser(String accessToken) {
        User user = getUserFromAccessToken(accessToken);
        return mapper.toDTO(user);

    }

    @Override
    public UserResponseDto updateUser(UpdateUserInfoRequestDto updateUserInfoRequestDto, String accessToken) {
        User user = getUserFromAccessToken(accessToken);

        user.setFirstName(updateUserInfoRequestDto.getFirstName());
        user.setLastName(updateUserInfoRequestDto.getLastName());
        user.setPhone(updateUserInfoRequestDto.getPhone());

        userRepository.save(user);
        return mapper.toDTO(user);
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
    }

    private User getUserFromAccessToken(String accessToken) {
        if(!jwtService.isTokenValid(accessToken)) {
            throw new JwtException(ErrorCode.JWT_IS_INVALID.getMessage());
        }

        String userId = jwtService.getUsername(accessToken);
        return userRepository.findById(userId)
                .orElseThrow(() -> new JwtException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }
}
