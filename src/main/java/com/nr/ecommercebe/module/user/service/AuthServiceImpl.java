package com.nr.ecommercebe.module.user.service;

import com.nr.ecommercebe.common.exception.ErrorCode;
import com.nr.ecommercebe.common.exception.RecordNotFoundException;
import com.nr.ecommercebe.common.service.CommonExceptionService;
import com.nr.ecommercebe.module.user.api.*;
import com.nr.ecommercebe.module.user.model.TokenType;
import com.nr.ecommercebe.module.user.model.User;
import com.nr.ecommercebe.module.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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



        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .user(mapper.toDTO(userDetails.user()))
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
        String userEmail = jwtService.getUsername(refreshToken);
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(
                () -> new RecordNotFoundException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage())
        );
        return jwtService.generateAccessToken(new CustomUserDetails(user));
    }

    @Override
    public void logout(String token) {
        // Implement blacklist token logic
    }
}
