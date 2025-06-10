package com.github.yaroglek.edudiary.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"lessons"})
@EqualsAndHashCode(exclude = {"lessons"})
@Entity
@Table(name = "schedule_day")
public class ScheduleDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @OneToMany(mappedBy = "scheduleDay", cascade = CascadeType.ALL)
    private Set<Lesson> lessons = new HashSet<>();
}

