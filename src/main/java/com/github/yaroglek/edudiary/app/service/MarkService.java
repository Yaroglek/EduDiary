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

    public Mark create(Mark mark) {
        if (mark == null) {
            throw new IllegalArgumentException("Mark is null");
        }

        Mark saved = markRepository.save(mark);
        log.info("Создана оценка {} ученику {} за урок {}", saved.getMarkValue(),
                saved.getStudent().getUsername(), saved.getLesson().getId());
        return saved;
    }

    public Mark getById(Long id) {
        Mark mark = markRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Оценка с id={} не найдена", id);
                    return new NoSuchElementException("Mark not found");
                });

        log.info("Получена оценка id={} значением {}", id, mark.getMarkValue());
        return mark;
    }

    public void deleteById(Long id) {
        log.info("Удаление оценки с id={}", id);
        markRepository.deleteById(id);
    }
}

