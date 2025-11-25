package com.digiconnect.pro.authentication.dtos;

import com.digiconnect.pro.authentication.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequestDto {
    private Long id;
    @NotNull(message = "Role name can not be null")
    private RoleType name;
}
