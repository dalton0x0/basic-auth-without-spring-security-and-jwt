package com.digiconnect.pro.authentication.services;

import com.digiconnect.pro.authentication.dtos.RoleRequestDto;
import com.digiconnect.pro.authentication.dtos.RoleResponseDto;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getAllRoles(String sessionToken);
    RoleResponseDto getRoleById(Long id, String sessionToken);
    RoleResponseDto createRole(RoleRequestDto roleRequestDto, String sessionToken);
    void deleteRoleById(Long id, String sessionToken);
}
