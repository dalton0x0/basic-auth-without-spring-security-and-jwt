package com.digiconnect.pro.authentication.services;

import com.digiconnect.pro.authentication.dtos.LoginRequestDto;
import com.digiconnect.pro.authentication.dtos.LoginResponseDto;
import com.digiconnect.pro.authentication.models.User;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    void logout(String sessionToken);
    User getAuthenticatedUser(String sessionToken);
    boolean isSessionValid(String sessionToken);
    LoginResponseDto.UserInfoDto getUserInfoDto(User user);
}
