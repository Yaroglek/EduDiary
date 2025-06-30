package com.github.yaroglek.edudiary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString(exclude = {"scheduleDay", "classSubject", "marks"})
@EqualsAndHashCode(exclude = {"scheduleDay", "classSubject", "marks"})
@Entity
@Table(name = "lesson",
        uniqueConstraints = @UniqueConstraint(columnNames = {"schedule_day_id", "lesson_number", "class_subject_id"}))
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_number", nullable = false)
    private Integer lessonNumber;

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

