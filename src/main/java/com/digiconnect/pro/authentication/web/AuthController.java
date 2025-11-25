package com.digiconnect.pro.authentication.web;

import com.digiconnect.pro.authentication.dtos.LoginRequestDto;
import com.digiconnect.pro.authentication.dtos.LoginResponseDto;
import com.digiconnect.pro.authentication.models.User;
import com.digiconnect.pro.authentication.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponse = authService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Session-Token") String sessionToken) {
        authService.logout(sessionToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponseDto.UserInfoDto> getCurrentUser(@RequestHeader("X-Session-Token") String sessionToken) {
        User user = authService.getAuthenticatedUser(sessionToken);
        LoginResponseDto.UserInfoDto userInfo = authService.getUserInfoDto(user);
        return ResponseEntity.ok(userInfo);
    }
}
