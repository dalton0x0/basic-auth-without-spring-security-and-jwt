package com.digiconnect.pro.authentication.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDto {

    private Long id;

    @NotNull(message = "Fist name can not be null")
    @NotBlank(message = "Fist name is required")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Only letters")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Only letters")
    private String lastName;

    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
            message = "Email is not valide")
    private String email;

    @NotNull(message = "Username can not be null")
    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Password can not be null")
    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Address can not be null")
    @NotBlank(message = "Address is is required")
    private String address;

    @NotNull(message = "Postal code can not be null")
    @NotBlank(message = "Postal code is is required")
    @Pattern(regexp = "^\\d{5}$", message = "Postal code must have 5 number")
    private String postalCode;

    @NotNull(message = "City can not be null")
    @NotBlank(message = "City is is required")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Only letters")
    private String city;

    @NotNull(message = "Country can not be null")
    @NotBlank(message = "Country is required")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Only letters")
    private String country;
}
