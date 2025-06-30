package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.domain.users.Admin;
import com.github.yaroglek.edudiary.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    /**
     * Создание администратора.
     */
    public Admin create(Admin admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Admin is null");
        }

        Admin saved = userRepository.save(admin);
        log.info("Создан администратор: {}", saved.getUsername());
        return saved;
    }

    /**
     * Получение администратора по ID.
     */
    public Admin getById(Long id) {
        Admin admin = (Admin) userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Администратор с id={} не найден", id);
                    return new NoSuchElementException("Admin not found");
                });

        log.info("Получен администратор: {}", admin.getUsername());
        return admin;
    }

    /**
     * Удаление администратора по ID.
     */
    public void deleteById(Long id) {
        log.info("Удаление администратора с id={}", id);
        userRepository.deleteById(id);
    }
}
