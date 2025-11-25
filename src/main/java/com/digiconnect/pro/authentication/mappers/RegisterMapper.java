package com.digiconnect.pro.authentication.mappers;

import com.digiconnect.pro.authentication.dtos.RegisterRequestDto;
import com.digiconnect.pro.authentication.dtos.RegisterResponseDto;
import com.digiconnect.pro.authentication.models.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    public User convertDtoToEntity(RegisterRequestDto registerRequestDto) {

        return User.builder()
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .email(registerRequestDto.getEmail())
                .username(registerRequestDto.getUsername())
                .password(registerRequestDto.getPassword())
                .address(registerRequestDto.getAddress())
                .postalCode(registerRequestDto.getPostalCode())
                .city(registerRequestDto.getCity())
                .country(registerRequestDto.getCountry())
                .build();
    }

    public RegisterResponseDto convertEntityToDto(User user) {
        return RegisterResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().getName().name())
                .build();
    }
}
