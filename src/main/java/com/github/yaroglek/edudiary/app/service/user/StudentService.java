package com.github.yaroglek.edudiary.app.service.user;

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

    /**
     * Метод для сохранения пользователя-ученика
     * @param student - ученик для сохранения
     * @return - сохраненный ученик
     */
    public Student create(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student is null");
        }

        Student saved = userRepository.save(student);
        log.info("Student with id {} created: {}", saved.getId(), saved.getUsername());
        return saved;
    }

    /**
     * Метод для получения ученика по ID
     * @param id - ID ученика
     * @return - найденный ученик
     */
    public Student getById(Long id) {
        Student student = (Student) userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student with id {} not found", id);
                    return new NoSuchElementException("Student not found");
                });

        log.info("Student with id {} found: {}", student.getId(), student.getUsername());
        return student;
    }

    /**
     * Метод для удаления ученика по ID
     * @param id - ID ученика
     */
    public void deleteById(Long id) {
        log.info("Student with id {} deleted", id);
        userRepository.deleteById(id);
    }
}
