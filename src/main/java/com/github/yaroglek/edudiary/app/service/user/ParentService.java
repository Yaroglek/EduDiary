package com.github.yaroglek.edudiary.app.service.user;

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

    /**
     * Метод для сохранения пользователя-родителя
     * @param parent - родитель для сохранения
     * @return - сохраненный родитель
     */
    public Parent create(Parent parent) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent is null");
        }

        Parent saved = userRepository.save(parent);
        log.info("Parent with id {} created: {}", saved.getId(), saved.getUsername());
        return saved;
    }

    /**
     * Метод для получения родителя по ID
     * @param id - ID родителя
     * @return - найденный родитель
     */
    public Parent getById(Long id) {
        Parent parent = (Parent) userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Parent with id {} not found", id);
                    return new NoSuchElementException("Parent not found");
                });

        log.info("Parent with id {} found: {}", parent.getId(), parent.getUsername());
        return parent;
    }

    /**
     * Метод для удаления родителя по ID
     * @param id - ID родителя
     */
    public void deleteById(Long id) {
        log.info("Parent with id {} deleted", id);
        userRepository.deleteById(id);
    }
}

