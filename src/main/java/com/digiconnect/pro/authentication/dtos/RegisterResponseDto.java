package com.digiconnect.pro.authentication.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String username;
    private String role;
}
