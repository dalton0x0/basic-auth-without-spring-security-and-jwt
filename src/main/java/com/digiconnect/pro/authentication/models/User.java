package com.digiconnect.pro.authentication.models;

import com.digiconnect.pro.authentication.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "role")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Builder.Default
    private Boolean emailVerified = false;

    @Builder.Default
    private Boolean isConnected = false;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime emailVerifiedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastLoginAt;

    @Column(unique = true)
    private String sessionToken;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime sessionExpiry;

    @Column(unique = true)
    private String emailVerificationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime emailVerificationExpiry;

    @PrePersist
    public void oncreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onupdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Boolean isUser() {
        return this.role.getName() == RoleType.USER;
    }

    public Boolean isAdmin() {
        return this.role.getName() == RoleType.ADMIN;
    }
}
