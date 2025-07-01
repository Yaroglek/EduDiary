package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Teacher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TeacherService extends GenericUserService<Teacher> {
    public TeacherService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder, Teacher.class);
    }
}