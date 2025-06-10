package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findByScheduleDayIdAndLessonNumberAndClassSubjectId(
            Long scheduleDayId, Integer lessonNumber, Long classSubjectId);

    List<Lesson> findByScheduleDayId(Long scheduleDayId);

    List<Lesson> findByClassSubjectId(Long classSubjectId);
}
