package com.digiconnect.pro.authentication.services;

import com.digiconnect.pro.authentication.dtos.RoleRequestDto;
import com.digiconnect.pro.authentication.dtos.RoleResponseDto;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto getRoleById(Integer id);
    RoleResponseDto createRole(RoleRequestDto roleRequestDto);
    void deleteRoleById(Integer id);
}
