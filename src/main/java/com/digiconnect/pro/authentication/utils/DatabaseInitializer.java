package com.digiconnect.pro.authentication.utils;

import com.digiconnect.pro.authentication.enums.RoleType;
import com.digiconnect.pro.authentication.models.Role;
import com.digiconnect.pro.authentication.models.User;
import com.digiconnect.pro.authentication.repositories.RoleRepository;
import com.digiconnect.pro.authentication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info("Initializing roles for database");
        Role adminRole = Role.builder().name(RoleType.ADMIN).build();
        Role userRole = Role.builder().name(RoleType.USER).build();
        roleRepository.save(adminRole);
        roleRepository.save(userRole);
        log.info("Roles for database initialized");
        log.info("Roles created : {}, {}", adminRole.getName(), userRole.getName());

        log.info("Initializing user for database");
        String sessionToken = UUID.randomUUID() + "-" + System.currentTimeMillis();
        User admin = User.builder()
                .firstName("DigiPro")
                .lastName("Administrator")
                .email("adminRole@digipro.com")
                .username("admin")
                .password("secret")
                .address("190 bis Boulevard de Charonne")
                .postalCode("75020")
                .city("Paris")
                .country("France")
                .role(adminRole)
                .emailVerified(true)
                .isConnected(true)
                .sessionToken(sessionToken)
                .sessionExpiry(LocalDateTime.now().plusHours(24))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(admin);
        log.info("User for database initialized");
        log.info("User created : {} : {}", admin.getFullName(), admin.getUsername());
        log.info("Session token for database initialized {}", sessionToken);
    }
}
