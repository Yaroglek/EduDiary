package com.github.yaroglek.edudiary.app.service.users;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;

    public Student create(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student is null");
        }

        Student saved = userRepository.save(student);
        log.info("Создан ученик: {}", saved.getUsername());
        return saved;
    }

    public Student getById(Long id) {
        Student student = (Student) userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ученик с id={} не найден", id);
                    return new NoSuchElementException("Student not found");
                });

        log.info("Получен ученик: {}", student.getUsername());
        return student;
    }

    public void deleteById(Long id) {
        log.info("Удаление ученика с id={}", id);
        userRepository.deleteById(id);
    }
}
