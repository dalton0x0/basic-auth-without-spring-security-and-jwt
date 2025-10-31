package com.digiconnect.pro.authentication.services.impl;

import com.digiconnect.pro.authentication.dtos.RoleRequestDto;
import com.digiconnect.pro.authentication.dtos.RoleResponseDto;
import com.digiconnect.pro.authentication.enums.RoleType;
import com.digiconnect.pro.authentication.exceptions.BadRequestException;
import com.digiconnect.pro.authentication.exceptions.NoSuchRoleException;
import com.digiconnect.pro.authentication.exceptions.RoleNameAlreadyInUseException;
import com.digiconnect.pro.authentication.mappers.RoleMapper;
import com.digiconnect.pro.authentication.models.Role;
import com.digiconnect.pro.authentication.repositories.RoleRepository;
import com.digiconnect.pro.authentication.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponseDto> getAllRoles() {
        log.debug("Getting all roles");
        List<RoleResponseDto> roles = roleRepository.findAll()
                .stream()
                .map(roleMapper::convertEntityToDto)
                .toList();
        log.info("Total roles get : {}, roles found: {}", roles.size(),  roles);
        return roles;
    }

    @Override
    public RoleResponseDto getRoleById(Integer id) {
        log.debug("Getting role with id : {}", id);
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchRoleException("Role not found"));
        log.info("Role get : {}", existingRole.getName());
        return roleMapper.convertEntityToDto(existingRole);
    }

    @Override
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        log.info("Creating role : {}", roleRequestDto);
        roleRepository.findByName(roleRequestDto.getName()).ifPresent(existingRole -> {
            throw new RoleNameAlreadyInUseException("Role already exists");
        });
        Role role = roleMapper.convertDtoToEntity(roleRequestDto);
        Role savedRole = roleRepository.save(role);
        log.info("Saved role : {}", savedRole);
        return roleMapper.convertEntityToDto(savedRole);
    }

    @Override
    public void deleteRoleById(Integer id) {
        log.info("Deleting role with id : {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchRoleException("Role not found"));
        if (role.getName().equals(RoleType.ADMIN)) {
            throw new BadRequestException("Cannot delete admin role");
        }
        roleRepository.deleteById(role.getId());
        log.info("Deleted role : {}", role.getName());
    }
}
