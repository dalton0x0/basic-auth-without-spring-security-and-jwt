package com.digiconnect.pro.authentication.web;

import com.digiconnect.pro.authentication.dtos.UpdateUserRequestDto;
import com.digiconnect.pro.authentication.dtos.UserResponseDto;
import com.digiconnect.pro.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(@RequestHeader("X-Session-Token") String sessionToken) {
        List<UserResponseDto> users = userService.getAllUsers(sessionToken);
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @RequestHeader("X-Session-Token") String sessionToken,
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id, sessionToken));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUser(
            @RequestHeader("X-Session-Token") String sessionToken,
            @RequestParam String lastName) {
        List<UserResponseDto> users = userService.searchUser(lastName, sessionToken);
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @RequestHeader("X-Session-Token") String sessionToken,
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequestDto, sessionToken));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(
            @RequestHeader("X-Session-Token") String sessionToken,
            @PathVariable Long id) {
        userService.deleteUserById(id, sessionToken);
        return ResponseEntity.noContent().build();
    }
}
