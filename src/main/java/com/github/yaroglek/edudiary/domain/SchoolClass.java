package com.github.yaroglek.edudiary.domain;

import com.github.yaroglek.edudiary.domain.users.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"students", "classSubjects"})
@EqualsAndHashCode(exclude = {"students", "classSubjects"})
@Entity
@Table(name = "school_class", uniqueConstraints = @UniqueConstraint(columnNames = {"grade", "literal"}))
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "literal", nullable = false, length = 1, columnDefinition = "CHAR")
    private String literal;

    @OneToMany(mappedBy = "schoolClass")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "schoolClass")
    private Set<ClassSubject> classSubjects = new HashSet<>();
}

