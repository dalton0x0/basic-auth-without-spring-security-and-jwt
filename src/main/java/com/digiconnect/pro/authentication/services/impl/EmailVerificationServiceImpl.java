package com.digiconnect.pro.authentication.services.impl;

import com.digiconnect.pro.authentication.dtos.VerifyEmailRequestDto;
import com.digiconnect.pro.authentication.exceptions.BadRequestException;
import com.digiconnect.pro.authentication.exceptions.InvalidTokenException;
import com.digiconnect.pro.authentication.exceptions.NoSuchUserException;
import com.digiconnect.pro.authentication.models.User;
import com.digiconnect.pro.authentication.repositories.UserRepository;
import com.digiconnect.pro.authentication.services.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final UserRepository userRepository;

    @Override
    public void generateEmailVerificationToken(User user) {

        log.info("Generating email verification token for user {}", user.getUsername());

        String token = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDateTime expiry = LocalDateTime.now().plusHours(24);

        user.setEmailVerificationToken(token);
        user.setEmailVerificationExpiry(expiry);

        userRepository.save(user);

        sendVerificationEmail(user, token);
    }

    @Override
    public boolean verifyEmail(VerifyEmailRequestDto verifyEmailRequestDto) {

        log.info("Trying email verification for : {}", verifyEmailRequestDto.getEmail());

        User user = userRepository.findByEmail(verifyEmailRequestDto.getEmail())
                .orElseThrow(() -> new NoSuchUserException("User not found"));

        if (user.getEmailVerified()) {
            log.info("Email verification has been verified");
            throw new BadRequestException("Email already verified");
        }

        if (user.getEmailVerificationToken() == null ||
                user.getEmailVerificationExpiry() == null) {
            log.info("Email verification token has not valid");
            throw new InvalidTokenException("No verification token found");
        }

        if (user.getEmailVerificationExpiry().isBefore(LocalDateTime.now())) {
            log.info("Email verification has expired");
            throw new InvalidTokenException("Verification token expired");
        }

        if (!user.getEmailVerificationToken().equals(verifyEmailRequestDto.getToken())) {
            log.info("Email verification token does not match");
            throw new InvalidTokenException("Invalid verification token");
        }

        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiry(null);

        userRepository.save(user);

        log.info("Email verified successfully for: {}", user.getEmail());

        return true;
    }

    @Override
    public void sendVerificationEmail(User user, String token) {
        log.info("Email verification generated for user : {}", user.getFullName());
        log.info("Email send to : {}", user.getEmail());
        log.info("Token to verify email : {}", token);
    }
}
