package com.github.yaroglek.edudiary.app.service.users;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Parent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ParentService {

    private final UserRepository userRepository;

    public Parent create(Parent parent) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent is null");
        }

        Parent saved = userRepository.save(parent);
        log.info("Создан родитель: {}", saved.getUsername());
        return saved;
    }

    public Parent getById(Long id) {
        Parent parent = (Parent) userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Родитель с id={} не найден", id);
                    return new NoSuchElementException("Parent not found");
                });

        log.info("Получен родитель: {}", parent.getUsername());
        return parent;
    }

    public void deleteById(Long id) {
        log.info("Удаление родителя с id={}", id);
        userRepository.deleteById(id);
    }
}

