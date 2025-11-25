package com.digiconnect.pro.authentication.services;

import com.digiconnect.pro.authentication.dtos.RegisterRequestDto;
import com.digiconnect.pro.authentication.dtos.RegisterResponseDto;

public interface RegisterService {
    RegisterResponseDto register(RegisterRequestDto requestDto);
}
