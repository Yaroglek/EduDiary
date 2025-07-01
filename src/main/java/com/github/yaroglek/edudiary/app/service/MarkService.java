package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.MarkRepository;
import com.github.yaroglek.edudiary.domain.Mark;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;

    /**
     * Метод для сохранения новой оценки
     *
     * @param mark - оценка для сохранения
     * @return - сохраненная оценка
     */
    public Mark create(Mark mark) {
        if (mark == null) {
            throw new IllegalArgumentException("Mark is null");
        }

        Mark saved = markRepository.save(mark);
        log.info("Mark with value {} created for student {} and lesson {}",
                saved.getMarkValue(), saved.getStudent().getUsername(), saved.getLesson().getId());
        return saved;
    }

    /**
     * Метод для получения оценки по ID
     *
     * @param id - ID оценки
     * @return - найденная оценка
     */
    public Mark getById(Long id) {
        Mark mark = markRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mark with id {} not found", id);
                    return new NoSuchElementException("Mark not found");
                });

        log.info("Mark with id {} found with value {}", id, mark.getMarkValue());
        return mark;
    }

    /**
     * Метод для обновления оценки. Меняется только значение и комментарий.
     * @param id - ID оценки
     * @param updatedMark - обновленная оценка
     * @return - сохраненная оценка
     */
    public Mark update(Long id, Mark updatedMark) {
        if (updatedMark == null) {
            throw new IllegalArgumentException("Mark is null");
        }

        Mark existingMark = getById(id);

        existingMark.setMarkValue(updatedMark.getMarkValue());
        existingMark.setComment(updatedMark.getComment());

        Mark saved = markRepository.save(existingMark);
        log.info("Mark with id {} updated", saved.getId());
        return saved;
    }

    /**
     * Метод для удаления оценки по ID
     *
     * @param id - ID оценки
     */
    public void deleteById(Long id) {
        log.info("Mark with id {} deleted", id);
        markRepository.deleteById(id);
    }
}

