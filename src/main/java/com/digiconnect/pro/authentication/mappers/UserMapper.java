package com.digiconnect.pro.authentication.mappers;

import com.digiconnect.pro.authentication.dtos.UpdateUserRequestDto;
import com.digiconnect.pro.authentication.dtos.UserResponseDto;
import com.digiconnect.pro.authentication.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto convertEntityToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .address(user.getAddress())
                .postalCode(user.getPostalCode())
                .city(user.getCity())
                .country(user.getCountry())
                .role(user.getRole().getName().name())
                .isConnected(user.getIsConnected())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    public void updateEntityFromDto(User user, UpdateUserRequestDto updateDto) {
        if (updateDto.getFirstName().trim().isEmpty()) {
            user.setFirstName(user.getFirstName());
        }
        if (updateDto.getLastName() == null || updateDto.getLastName().trim().isEmpty()) {
            user.setLastName(user.getLastName());
        }
        if (updateDto.getEmail() == null || updateDto.getEmail().trim().isEmpty()) {
            user.setEmail(user.getEmail());
        }
        if (updateDto.getUsername() == null || updateDto.getUsername().trim().isEmpty()) {
            user.setUsername(user.getUsername());
        }
        if (updateDto.getPassword() == null || updateDto.getPassword().trim().isEmpty()) {
            user.setPassword(user.getPassword());
        }
        if (updateDto.getAddress() == null || updateDto.getAddress().trim().isEmpty()) {
            user.setAddress(user.getAddress());
        }
        if (updateDto.getPostalCode() == null || updateDto.getPostalCode().trim().isEmpty()) {
            user.setPostalCode(user.getPostalCode());
        }
        if (updateDto.getCity() == null || updateDto.getCity().trim().isEmpty()) {
            user.setCity(user.getCity());
        }
        if (updateDto.getCountry() == null || updateDto.getCountry().trim().isEmpty()) {
            user.setCountry(user.getCountry());
        }
        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setEmail(updateDto.getEmail());
        user.setUsername(updateDto.getUsername());
        user.setPassword(updateDto.getPassword());
        user.setAddress(updateDto.getAddress());
        user.setPostalCode(updateDto.getPostalCode());
        user.setCity(updateDto.getCity());
        user.setCountry(updateDto.getCountry());
    }
}
