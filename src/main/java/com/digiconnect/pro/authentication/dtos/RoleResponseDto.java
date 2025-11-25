package com.digiconnect.pro.authentication.dtos;

import com.digiconnect.pro.authentication.enums.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponseDto {
    private Long id;
    private RoleType name;
}
