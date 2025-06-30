package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Teacher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;

    public Teacher create(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher is null");
        }

        Teacher saved = userRepository.save(teacher);
        log.info("Создан учитель: {}", saved.getUsername());
        return saved;
    }

    public Teacher getById(Long id) {
        Teacher teacher = (Teacher) userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Учитель с id={} не найден", id);
                    return new NoSuchElementException("Teacher not found");
                });

        log.info("Получен учитель: {}", teacher.getUsername());
        return teacher;
    }

    public void deleteById(Long id) {
        log.info("Удаление учителя с id={}", id);
        userRepository.deleteById(id);
    }
}
