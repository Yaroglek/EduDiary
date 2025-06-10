package com.github.yaroglek.edudiary.domain;

import com.github.yaroglek.edudiary.domain.user.Student;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = {"lesson", "student"})
@EqualsAndHashCode(exclude = {"lesson", "student"})
@Entity
@Table(name = "mark",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "lesson_id"}))
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer value;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}

