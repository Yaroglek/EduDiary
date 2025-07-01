package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("""
                SELECT l FROM Lesson l
                WHERE l.classSubject.teacher.id = :teacherId
                  AND l.scheduleDay.date BETWEEN :startOfWeek AND :endOfWeek
            """)
    List<Lesson> findLessonsForTeacherInWeek(
            @Param("teacherId") Long teacherId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek);
}
