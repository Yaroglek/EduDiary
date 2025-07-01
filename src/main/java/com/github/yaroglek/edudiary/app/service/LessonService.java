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

    /**
     * Метод для сохранения урока
     *
     * @param lesson - урок для сохранения
     * @return - сохраненный урок
     */
    public Lesson create(Lesson lesson) {
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson is null");
        }

        Lesson saved = lessonRepository.save(lesson);
        log.info("Lesson number {} created for date {}", saved.getLessonNumber(), saved.getScheduleDay().getDate());
        return saved;
    }

    /**
     * Метод для получения урока по ID
     *
     * @param id - ID урока
     * @return - найденный урок
     */
    public Lesson getById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Lesson with id {} not found", id);
                    return new NoSuchElementException("Lesson not found");
                });

        log.info("Lesson with id {} found", lesson.getId());
        return lesson;
    }

    /**
     * Метод для удаления урока по ID
     *
     * @param id - ID урока
     */
    public void deleteById(Long id) {
        log.info("Lesson with id {} deleted", id);
        lessonRepository.deleteById(id);
    }
}

