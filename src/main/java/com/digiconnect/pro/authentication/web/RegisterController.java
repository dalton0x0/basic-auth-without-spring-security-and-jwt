package com.digiconnect.pro.authentication.web;

import com.digiconnect.pro.authentication.dtos.VerifyEmailRequestDto;
import com.digiconnect.pro.authentication.dtos.RegisterRequestDto;
import com.digiconnect.pro.authentication.dtos.RegisterResponseDto;
import com.digiconnect.pro.authentication.services.EmailVerificationService;
import com.digiconnect.pro.authentication.services.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterService registerService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("")
    public ResponseEntity<RegisterResponseDto>  register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        RegisterResponseDto registerResponseDto = registerService.register(registerRequestDto);
        return ResponseEntity.ok(registerResponseDto);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerifyEmailRequestDto verifyEmailRequestDto) {
        boolean verified = emailVerificationService.verifyEmail(verifyEmailRequestDto);
        return verified ? ResponseEntity.ok("Email verified successfully") :
                ResponseEntity.badRequest().body("Email verification failed");
    }
}
