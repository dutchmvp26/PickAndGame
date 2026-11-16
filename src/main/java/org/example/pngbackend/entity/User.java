package org.example.pngbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

    @Entity
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 30)
        private String username;

        @Column(nullable = false)
        private String passwordHash;

        @Column(nullable = false)
        private LocalDateTime createdAt;

        public User() {}

        // getters and setters for all four fields (same style as Room)
    }
