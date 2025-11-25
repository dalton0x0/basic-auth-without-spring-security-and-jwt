package com.digiconnect.pro.authentication.repositories;

import com.digiconnect.pro.authentication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findBySessionToken(String sessionToken);
    List<User> findByLastNameContainingIgnoreCase(String lastName);
}
