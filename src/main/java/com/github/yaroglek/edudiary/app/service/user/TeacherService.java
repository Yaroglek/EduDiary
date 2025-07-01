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

    /**
     * Метод для сохранения пользователя-учителя
     * @param teacher - учитель для сохранения
     * @return - сохраненный учитель
     */
    public Teacher create(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher is null");
        }

        Teacher saved = userRepository.save(teacher);
        log.info("Teacher with id {} created: {}", saved.getId(), saved.getUsername());
        return saved;
    }

    /**
     * Метод для получения учителя по ID
     * @param id - ID для получения
     * @return - найденный учитель
     */
    public Teacher getById(Long id) {
        Teacher teacher = (Teacher) userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Teacher with id {} not found", id);
                    return new NoSuchElementException("Teacher not found");
                });

        log.info("Teacher with id {} found: {}", teacher.getId(), teacher.getUsername());
        return teacher;
    }

    /**
     * Метод для удаления учителя по ID
     * @param id - ID для удаления
     */
    public void deleteById(Long id) {
        log.info("Teacher with id {} deleted", id);
        userRepository.deleteById(id);
    }
}
