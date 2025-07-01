package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.domain.users.Admin;
import com.github.yaroglek.edudiary.app.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends GenericUserService<Admin> {
    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder, Admin.class);
    }

    @PostConstruct
    private void createFirstSuperUser() {
        Admin superUser = new Admin();
        superUser.setUsername("superuser");
        superUser.setPassword("superuser");
        superUser.setEmail("superuser@yarogle.com");
        superUser.setFullName("Super User");
        create(superUser);
    }
}
