package com.github.yaroglek.edudiary.domain;

import com.github.yaroglek.edudiary.domain.users.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString(exclude = {"students", "classSubjects"})
@EqualsAndHashCode(exclude = {"students", "classSubjects"})
@Entity
@Table(name = "school_class", uniqueConstraints = @UniqueConstraint(columnNames = {"grade", "literal"}))
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false, length = 1)
    private String literal;

    @OneToMany(mappedBy = "schoolClass")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "schoolClass")
    private Set<ClassSubject> classSubjects = new HashSet<>();
}

