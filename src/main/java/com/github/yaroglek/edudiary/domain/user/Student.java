package com.github.yaroglek.edudiary.domain.user;

import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"user", "marks", "parents", "schoolClass"})
@EqualsAndHashCode(exclude = {"user", "marks", "parents", "schoolClass"})
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;

    @ManyToMany(mappedBy = "children")
    private Set<Parent> parents = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Mark> marks = new HashSet<>();
}
