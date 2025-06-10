package com.github.yaroglek.edudiary.domain;

import com.github.yaroglek.edudiary.domain.user.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"schoolClass", "subject", "teacher", "lessons"})
@EqualsAndHashCode(exclude = {"schoolClass", "subject", "teacher", "lessons"})
@Entity
@Table(name = "class_subject",
        uniqueConstraints = @UniqueConstraint(columnNames = {"class_id", "subject_id"}))
public class ClassSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "classSubject", cascade = CascadeType.ALL)
    private Set<Lesson> lessons = new HashSet<>();
}

