package com.github.yaroglek.edudiary.domain.user;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Data
@ToString(exclude = {"user", "children"})
@EqualsAndHashCode(exclude = {"user", "children"})
@Entity
@Table(name = "parents")
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "parent_student",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> children = new HashSet<>();
}
