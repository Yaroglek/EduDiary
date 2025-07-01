package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Parent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ParentService extends GenericUserService<Parent> {
    public ParentService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder, Parent.class);
    }
}

