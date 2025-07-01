package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
