package com.digiconnect.pro.authentication.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyEmailRequestDto {

    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Token can not be null")
    @NotBlank(message = "Token is required")
    private String token;
}
