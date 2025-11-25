package com.digiconnect.pro.authentication.services.impl;

import com.digiconnect.pro.authentication.dtos.UpdateUserRequestDto;
import com.digiconnect.pro.authentication.dtos.UserResponseDto;
import com.digiconnect.pro.authentication.exceptions.InvalidTokenException;
import com.digiconnect.pro.authentication.exceptions.NoSuchUserException;
import com.digiconnect.pro.authentication.mappers.UserMapper;
import com.digiconnect.pro.authentication.models.User;
import com.digiconnect.pro.authentication.repositories.UserRepository;
import com.digiconnect.pro.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers(String sessionToken) {

        log.info("Getting all users");

        verifyRoleUserBySessionToken(sessionToken);

        List<UserResponseDto> users = userRepository.findAll()
                .stream()
                .map(userMapper::convertEntityToDto)
                .toList();

        log.info("Total users get : {}, users found : {}", users.size(),  users);

        return users;
    }

    @Override
    public UserResponseDto getUserById(Long userId, String sessionToken) {

        log.info("Getting user by id : {}", userId);

        verifyRoleUserBySessionToken(sessionToken);

        UserResponseDto existingUser = userMapper.convertEntityToDto(userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserException("User not found"))
        );

        log.info("User get : {}", existingUser.getFullName());

        return existingUser;
    }

    @Override
    public List<UserResponseDto> searchUser(String lastName, String sessionToken) {

        log.info("Searching users by keyword : {}", lastName);

        verifyRoleUserBySessionToken(sessionToken);

        if (lastName == null || lastName.trim().isEmpty()) {
            log.info("Trying to search users with empty or null lastname");
            getAllUsers(sessionToken);
        }

        List<UserResponseDto> results = userRepository.findByLastNameContainingIgnoreCase(lastName)
                .stream()
                .map(userMapper::convertEntityToDto)
                .toList();

        log.info("Total users found : {}, users found : {}", results.size(),  results);

        return results;
    }

    @Override
    public UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateDto, String sessionToken) {

        log.info("Updating user with id : {}", userId);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserException("User not found"));

        User user = userRepository.findBySessionToken(sessionToken).orElseThrow(
                () -> new InvalidTokenException("Invalid token session")
        );

        verifyIfItIsTheSameUserOrIsAdmin(sessionToken, existingUser, user);

        userMapper.updateEntityFromDto(existingUser, updateDto);

        User updatedUser = userRepository.save(existingUser);

        log.info("Updated user : {}", updatedUser.getFullName());

        return userMapper.convertEntityToDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long userId, String sessionToken) {

        log.info("Deleting user with id : {}", userId);

        verifyRoleUserBySessionToken(sessionToken);

        User userToDelete = userRepository.findById(userId)
                        .orElseThrow(() -> new NoSuchUserException("User not found"));

        userRepository.deleteById(userId);

        log.info("User deleted : {}", userToDelete.getFullName());
    }

    private void verifyRoleUserBySessionToken(String sessionToken) {
        User user = userRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid token session"));

        if (!user.isAdmin()) {
            log.error("User : {}, has not an admin role to do this action", user.getFullName());
            throw new InvalidTokenException("Only admins can do this");
        }
    }

    private void verifyIfItIsTheSameUserOrIsAdmin(String sessionToken, User existingUser, User user) {
        if (!existingUser.getSessionToken().equals(sessionToken) && !user.isAdmin()) {
            verifyRoleUserBySessionToken(sessionToken);
            log.error("User : {} is not allowed to update this user", user);
            log.error("Something not right, user has not the same as session token");
            log.error("Expected user : {}, found user : {}", existingUser.getFullName(), user.getFullName());
            log.error("Expected token : {}, found token : {}", existingUser.getSessionToken(), sessionToken);
            throw new InvalidTokenException("Session token user not match");
        }
    }
}
