package com.digiconnect.pro.authentication.services;

import com.digiconnect.pro.authentication.dtos.UpdateUserRequestDto;
import com.digiconnect.pro.authentication.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers(String sessionToken);
    UserResponseDto getUserById(Long userId, String sessionToken);
    List<UserResponseDto> searchUser(String keyword, String sessionToken);
    UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateDto, String sessionToken);
    void deleteUserById(Long userId, String sessionToken);
}
