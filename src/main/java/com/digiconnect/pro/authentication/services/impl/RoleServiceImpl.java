package com.digiconnect.pro.authentication.services.impl;

import com.digiconnect.pro.authentication.dtos.RoleRequestDto;
import com.digiconnect.pro.authentication.dtos.RoleResponseDto;
import com.digiconnect.pro.authentication.enums.RoleType;
import com.digiconnect.pro.authentication.exceptions.BadRequestException;
import com.digiconnect.pro.authentication.exceptions.InvalidTokenException;
import com.digiconnect.pro.authentication.exceptions.NoSuchRoleException;
import com.digiconnect.pro.authentication.exceptions.RoleNameAlreadyInUseException;
import com.digiconnect.pro.authentication.mappers.RoleMapper;
import com.digiconnect.pro.authentication.models.Role;
import com.digiconnect.pro.authentication.models.User;
import com.digiconnect.pro.authentication.repositories.RoleRepository;
import com.digiconnect.pro.authentication.repositories.UserRepository;
import com.digiconnect.pro.authentication.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponseDto> getAllRoles(String sessionToken) {

        log.debug("Getting all roles");

        verifyRoleUserBySessionToken(sessionToken);

        List<RoleResponseDto> roles = roleRepository.findAll()
                .stream()
                .map(roleMapper::convertEntityToDto)
                .toList();
        log.info("Total roles get : {}, roles found: {}", roles.size(),  roles);

        return roles;
    }

    @Override
    public RoleResponseDto getRoleById(Long id, String sessionToken) {

        log.debug("Getting role with id : {}", id);

        verifyRoleUserBySessionToken(sessionToken);

        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchRoleException("Role not found"));
        log.info("Role get : {}", existingRole.getName());

        return roleMapper.convertEntityToDto(existingRole);
    }

    @Override
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto, String sessionToken) {

        log.info("Creating role : {}", roleRequestDto);

        verifyRoleUserBySessionToken(sessionToken);

        roleRepository.findByName(roleRequestDto.getName()).ifPresent(existingRole -> {
            throw new RoleNameAlreadyInUseException("Role already exists");
        });
        Role role = roleMapper.convertDtoToEntity(roleRequestDto);
        Role savedRole = roleRepository.save(role);

        log.info("Saved role : {}", savedRole);

        return roleMapper.convertEntityToDto(savedRole);
    }

    @Override
    public void deleteRoleById(Long id, String sessionToken) {

        log.info("Deleting role with id : {}", id);

        verifyRoleUserBySessionToken(sessionToken);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchRoleException("Role not found"));
        if (role.getName().equals(RoleType.ADMIN)) {
            throw new BadRequestException("Cannot delete admin role");
        }

        roleRepository.deleteById(role.getId());

        log.info("Deleted role : {}", role.getName());
    }

    private void verifyRoleUserBySessionToken(String sessionToken) {
        User user = userRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid token session"));

        if (!user.isAdmin()) {
            log.error("User : {}, has not an admin role to do this action", user.getFullName());
            throw new InvalidTokenException("Only admins can do this");
        }
    }
}
