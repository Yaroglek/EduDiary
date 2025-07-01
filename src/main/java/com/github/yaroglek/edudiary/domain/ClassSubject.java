package com.github.yaroglek.edudiary.domain;

import com.github.yaroglek.edudiary.domain.users.Teacher;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"schoolClass", "subject", "teacher"})
@EqualsAndHashCode(exclude = {"schoolClass", "subject", "teacher"})
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
}

