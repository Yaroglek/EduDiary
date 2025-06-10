package com.github.yaroglek.edudiary.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@ToString(exclude = {"student", "teacher", "parent", "admin"})
@EqualsAndHashCode(exclude = {"student", "teacher", "parent", "admin"})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email",unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Teacher teacher;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Parent parent;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Admin admin;
}
