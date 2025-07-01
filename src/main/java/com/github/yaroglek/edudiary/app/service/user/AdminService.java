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
     * Метод для сохранения пользователя-администратора
     * @param admin - администратор для сохранения
     * @return - сохраненный администратор
     */
    public Admin create(Admin admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Admin is null");
        }

        Admin saved = userRepository.save(admin);
        log.info("Admin with id {} created: {}", saved.getId(), saved.getUsername());
        return saved;
    }

    /**
     * Метод для получения администратора по ID
     * @param id - ID администратора
     * @return - найденный администратор
     */
    public Admin getById(Long id) {
        Admin admin = (Admin) userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Admin with id {} not found", id);
                    return new NoSuchElementException("Admin not found");
                });

        log.info("Admin with id {} found: {}", admin.getId(), admin.getUsername());
        return admin;
    }

    /**
     * Метод для удаления администратора по ID
     * @param id - ID администратора
     */
    public void deleteById(Long id) {
        log.info("Admin with id {} deleted", id);
        userRepository.deleteById(id);
    }
}
