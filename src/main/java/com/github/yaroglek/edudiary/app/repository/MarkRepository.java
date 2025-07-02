package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findAllByLessonId(Long lessonId);
}
