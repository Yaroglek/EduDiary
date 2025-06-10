package com.github.yaroglek.edudiary.domain;

import com.github.yaroglek.edudiary.domain.user.Student;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"students", "classSubjects"})
@EqualsAndHashCode(exclude = {"students", "classSubjects"})
@Entity
@Table(name = "school_class")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 4) // 10A, 3Б
    private String name;

    @OneToMany(mappedBy = "schoolClass")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "schoolClass")
    private Set<ClassSubject> classSubjects = new HashSet<>();
}

