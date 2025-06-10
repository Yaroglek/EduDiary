package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    Optional<Mark> findByStudentIdAndLessonId(Long studentId, Long lessonId);
    List<Mark> findByStudentId(Long studentId);
    List<Mark> findByLessonId(Long lessonId);
}
