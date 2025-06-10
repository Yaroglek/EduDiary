package com.github.yaroglek.edudiary.domain.user;

import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"user", "subjects", "classSubjects"})
@EqualsAndHashCode(exclude = {"user", "subjects", "classSubjects"})
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<ClassSubject> classSubjects = new HashSet<>();
}
