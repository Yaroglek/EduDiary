package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.LessonRepository;
import com.github.yaroglek.edudiary.domain.Lesson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    public Lesson create(Lesson lesson) {
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson is null");
        }

        Lesson saved = lessonRepository.save(lesson);
        log.info("Создан урок №{} для даты {}", saved.getLessonNumber(), saved.getScheduleDay().getDate());
        return saved;
    }

    public Lesson getById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Урок с id={} не найден", id);
                    return new NoSuchElementException("Lesson not found");
                });

        log.info("Получен урок: {}", lesson.getId());
        return lesson;
    }

    public void deleteById(Long id) {
        log.info("Удаление урока с id={}", id);
        lessonRepository.deleteById(id);
    }
}

