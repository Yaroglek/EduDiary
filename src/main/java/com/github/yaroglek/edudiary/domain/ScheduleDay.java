package com.github.yaroglek.edudiary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@ToString(exclude = {"lessons"})
@EqualsAndHashCode(exclude = {"lessons"})
@Entity
@Table(name = "schedule_day")
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "scheduleDay", cascade = CascadeType.ALL)
    @OrderBy("lessonNumber ASC")
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;
}

