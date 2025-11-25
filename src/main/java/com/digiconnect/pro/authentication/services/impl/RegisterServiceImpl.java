package com.digiconnect.pro.authentication.services.impl;

import com.digiconnect.pro.authentication.dtos.RegisterRequestDto;
import com.digiconnect.pro.authentication.dtos.RegisterResponseDto;
import com.digiconnect.pro.authentication.enums.RoleType;
import com.digiconnect.pro.authentication.exceptions.NoSuchRoleException;
import com.digiconnect.pro.authentication.exceptions.UsernameAlreadyInUseException;
import com.digiconnect.pro.authentication.mappers.RegisterMapper;
import com.digiconnect.pro.authentication.models.Role;
import com.digiconnect.pro.authentication.models.User;
import com.digiconnect.pro.authentication.repositories.RoleRepository;
import com.digiconnect.pro.authentication.repositories.UserRepository;
import com.digiconnect.pro.authentication.services.EmailVerificationService;
import com.digiconnect.pro.authentication.services.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RegisterMapper registerMapper;
    private final EmailVerificationService emailVerificationService;

    @Override
    public RegisterResponseDto register(RegisterRequestDto requestDto) {

        log.info("Register request received : {}", requestDto);

        Optional<User> existingUsername = userRepository.findByUsername(requestDto.getUsername());
        Optional<User> existingEmail = userRepository.findByEmail(requestDto.getEmail());

        if(existingEmail.isPresent()) {
            log.info("Email already exists in database : {}", requestDto.getEmail());
            throw new UsernameAlreadyInUseException("Email is not available");
        }

        if(existingUsername.isPresent()) {
            log.info("Username already exists in database : {}", requestDto.getUsername());
            throw new UsernameAlreadyInUseException("Username is not available");
        }

        Role defaultRole = roleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> new NoSuchRoleException("Default role is not available"));

        User user = registerMapper.convertDtoToEntity(requestDto);
        user.setRole(defaultRole);

        User registeredUser = userRepository.save(user);

        emailVerificationService.generateEmailVerificationToken(registeredUser);

        log.info("Registered user : {}", registeredUser);

        return registerMapper.convertEntityToDto(registeredUser);
    }
}
