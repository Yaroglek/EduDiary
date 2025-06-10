package com.github.yaroglek.edudiary.domain;

import com.github.yaroglek.edudiary.domain.user.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"teachers", "classSubjects"})
@EqualsAndHashCode(exclude = {"teachers", "classSubjects"})
@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @ManyToMany(mappedBy = "subjects")
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private Set<ClassSubject> classSubjects = new HashSet<>();
}
