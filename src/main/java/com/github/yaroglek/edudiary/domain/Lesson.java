package com.github.yaroglek.edudiary.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"scheduleDay", "classSubject", "marks"})
@EqualsAndHashCode(exclude = {"scheduleDay", "classSubject", "marks"})
@Entity
@Table(name = "lesson",
        uniqueConstraints = @UniqueConstraint(columnNames = {"schedule_day_id", "lesson_number", "class_subject_id"}))
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_number", nullable = false)
    private Integer lessonNumber; // от 1 до 10

    @Column(name = "homework")
    private String homeworkDescription;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schedule_day_id", nullable = false)
    private ScheduleDay scheduleDay;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_subject_id", nullable = false)
    private ClassSubject classSubject;

    @OneToMany(mappedBy = "lesson")
    private Set<Mark> marks = new HashSet<>();
}

