package com.digiconnect.pro.authentication.web;

import com.digiconnect.pro.authentication.dtos.RoleRequestDto;
import com.digiconnect.pro.authentication.dtos.RoleResponseDto;
import com.digiconnect.pro.authentication.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("")
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<RoleResponseDto> roles = roleService.getAllRoles();
        return roles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Integer id) {
        RoleResponseDto role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    @PostMapping("")
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody @Valid RoleRequestDto roleRequestDto) {
        RoleResponseDto role = roleService.createRole(roleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleResponseDto> deleteRoleById(@PathVariable Integer id) {
        roleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }
}
