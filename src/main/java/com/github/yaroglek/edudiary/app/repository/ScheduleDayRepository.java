package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.ScheduleDay;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleDayRepository extends JpaRepository<ScheduleDay, Long> {
    Optional<ScheduleDay> findByDate(LocalDate date);
    Optional<ScheduleDay> findByDateAndSchoolClass(LocalDate date, SchoolClass schoolClass);
}
