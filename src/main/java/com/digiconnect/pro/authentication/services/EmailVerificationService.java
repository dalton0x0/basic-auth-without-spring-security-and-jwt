package com.digiconnect.pro.authentication.services;

import com.digiconnect.pro.authentication.dtos.VerifyEmailRequestDto;
import com.digiconnect.pro.authentication.models.User;

public interface EmailVerificationService {
    void generateEmailVerificationToken(User user);
    boolean verifyEmail(VerifyEmailRequestDto verifyEmailRequestDto);
    void sendVerificationEmail(User user, String token);
}
