package com.digiconnect.pro.authentication.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotNull(message = "Username or email can not be null")
    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    @NotNull(message = "Password can not be null")
    @NotBlank(message = "Password is required")
    private String password;
}
