package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.SubjectRepository;
import com.github.yaroglek.edudiary.domain.Subject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public Subject create(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject is null");
        }

        Subject saved = subjectRepository.save(subject);
        log.info("Создан предмет: {}", saved.getName());
        return saved;
    }

    public Subject getById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Предмет с id={} не найден", id);
                    return new NoSuchElementException("Subject not found");
                });

        log.info("Получен предмет: {}", subject.getName());
        return subject;
    }

    public void deleteById(Long id) {
        log.info("Удаление предмета с id={}", id);
        subjectRepository.deleteById(id);
    }
}

