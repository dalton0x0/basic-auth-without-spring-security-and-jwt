package com.digiconnect.pro.authentication.repositories;

import com.digiconnect.pro.authentication.enums.RoleType;
import com.digiconnect.pro.authentication.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
