package com.github.yaroglek.edudiary.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@ToString(exclude = {"user"})
@EqualsAndHashCode(exclude = {"user"})
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
