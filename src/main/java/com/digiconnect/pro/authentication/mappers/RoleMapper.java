package com.digiconnect.pro.authentication.mappers;

import com.digiconnect.pro.authentication.dtos.RoleRequestDto;
import com.digiconnect.pro.authentication.dtos.RoleResponseDto;
import com.digiconnect.pro.authentication.models.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role convertDtoToEntity(RoleRequestDto roleRequestDto) {
        return Role.builder()
                .id(roleRequestDto.getId())
                .name(roleRequestDto.getName())
                .build();
    }

    public RoleResponseDto convertEntityToDto(Role role) {
        return RoleResponseDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
