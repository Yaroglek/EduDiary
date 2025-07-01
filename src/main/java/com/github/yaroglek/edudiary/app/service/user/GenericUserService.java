package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

@Slf4j
@Transactional
@RequiredArgsConstructor
public abstract class GenericUserService<T extends User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Class<T> tClass;

    /**
     * Метод для сохранения пользователя
     */
    public T create(T user) {
        if (user == null) {
            throw new IllegalArgumentException(tClass.getSimpleName() + " is null");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        log.info("{} with id {} created: {}", tClass.getSimpleName(), saved.getId(), saved.getUsername());
        return tClass.cast(saved);
    }

    /**
     * Метод для получения пользователя по ID
     */
    public T getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{} with id {} not found", tClass.getSimpleName(), id);
                    return new NoSuchElementException(tClass.getSimpleName() + " not found");
                });

        log.info("{} with id {} found: {}", tClass.getSimpleName(), id, user.getUsername());
        return tClass.cast(user);
    }

    /**
     * Метод для обновления юзера.
     * @param id - ID юзера
     * @param updatedUser - обновленный юзер
     * @return - сохраненный юзер
     */
    public T update(Long id, T updatedUser) {
        if (updatedUser == null) {
            throw new IllegalArgumentException(tClass.getSimpleName() + " is null");
        }

        User existingUser = getById(id);

        existingUser.setUsername(updatedUser.getUsername());
        if (!updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFullName(updatedUser.getFullName());

        User saved = userRepository.save(existingUser);
        log.info("{} with id {} updated", tClass.getSimpleName(), saved.getId());

        return tClass.cast(saved);
    }

    /**
     * Метод для удаления пользователя по ID
     */
    public void deleteById(Long id) {
        log.info("{} with id {} deleted", tClass.getSimpleName(), id);
        userRepository.deleteById(id);
    }
}

