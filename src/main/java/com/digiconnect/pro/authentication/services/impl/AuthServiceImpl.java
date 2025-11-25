package com.digiconnect.pro.authentication.services.impl;

import com.digiconnect.pro.authentication.dtos.LoginRequestDto;
import com.digiconnect.pro.authentication.dtos.LoginResponseDto;
import com.digiconnect.pro.authentication.exceptions.AlreadyConnectedException;
import com.digiconnect.pro.authentication.exceptions.BadRequestException;
import com.digiconnect.pro.authentication.exceptions.InvalidTokenException;
import com.digiconnect.pro.authentication.exceptions.NoSuchUserException;
import com.digiconnect.pro.authentication.models.User;
import com.digiconnect.pro.authentication.repositories.UserRepository;
import com.digiconnect.pro.authentication.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        log.info("Trying login for: {}", loginRequestDto.getUsernameOrEmail());

        User user = userRepository.findByEmail(loginRequestDto.getUsernameOrEmail())
                .orElse(userRepository.findByUsername(loginRequestDto.getUsernameOrEmail())
                        .orElseThrow(() -> new NoSuchUserException("Invalid credentials")));

        if (!loginRequestDto.getPassword().equals(user.getPassword())) {
            log.error("Invalid credentials for user: {}", loginRequestDto.getUsernameOrEmail());
            throw new BadRequestException("Invalid username or password");
        }

        if (!user.getEmailVerified()) {
            log.info("Trying to login with email not verified : {}", loginRequestDto.getUsernameOrEmail());
            throw new BadRequestException("Email is not verified");
        }

        if (user.getSessionToken() != null) {
            log.info("User : {} is already connected", loginRequestDto.getUsernameOrEmail());
            throw new AlreadyConnectedException("You have already connected");
        }

        String sessionToken = generateSessionToken();
        LocalDateTime expiry = LocalDateTime.now().plusHours(24);

        user.setSessionToken(sessionToken);
        user.setSessionExpiry(expiry);
        user.setIsConnected(true);
        user.setLastLoginAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        log.info("User logged : {}", savedUser.getUsername());

        return LoginResponseDto.builder()
                .sessionToken(sessionToken)
                .expiry(expiry)
                .userInfo(LoginResponseDto.UserInfoDto.builder()
                        .id(savedUser.getId())
                        .fullName(savedUser.getFullName())
                        .email(savedUser.getEmail())
                        .username(savedUser.getUsername())
                        .role(savedUser.getRole().getName().name())
                        .build())
                .build();
    }

    @Override
    public void logout(String sessionToken) {

        log.info("Trying logout session for : {}", sessionToken);

        User user = userRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid session"));

        user.setSessionToken(null);
        user.setSessionExpiry(null);
        user.setIsConnected(false);

        userRepository.save(user);

        log.info("User logged out : {}", user.getUsername());
    }

    @Override
    public User getAuthenticatedUser(String sessionToken) {

        if (!isSessionValid(sessionToken)) {
            log.error("Invalid session token: {}", sessionToken);
            throw new InvalidTokenException("Invalid or expired session");
        }

        log.info("Trying to get authenticated user for : {}", sessionToken);

        return userRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new NoSuchUserException("User not found"));
    }

    @Override
    public boolean isSessionValid(String sessionToken) {

        if (sessionToken == null || sessionToken.trim().isEmpty()) {
            log.error("Not valid token: {}", sessionToken);
            return false;
        }

        log.info("Trying to check if session token is valid: {}", sessionToken);

        return userRepository.findBySessionToken(sessionToken)
                .map(user -> user.getSessionExpiry() != null &&
                        user.getSessionExpiry().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    private String generateSessionToken() {
        return UUID.randomUUID() + "-" + System.currentTimeMillis();
    }

    @Override
    public LoginResponseDto.UserInfoDto getUserInfoDto(User user) {

        log.info("Trying to get user connected info for : {}", user.getUsername());

        return LoginResponseDto.UserInfoDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().getName().name())
                .build();
    }
}
