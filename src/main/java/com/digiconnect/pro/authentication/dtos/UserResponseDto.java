package com.digiconnect.pro.authentication.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String username;
    private String address;
    private String postalCode;
    private String city;
    private String country;
    private String role;
    private Boolean isConnected;
    private LocalDateTime lastLoginAt;
}
