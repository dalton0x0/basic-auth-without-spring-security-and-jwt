package com.digiconnect.pro.authentication.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoginResponseDto {
    private String sessionToken;
    private LocalDateTime expiry;
    private UserInfoDto userInfo;

    @Data
    @Builder
    public static class UserInfoDto {
        private Long id;
        private String fullName;
        private String email;
        private String username;
        private String role;
    }
}
