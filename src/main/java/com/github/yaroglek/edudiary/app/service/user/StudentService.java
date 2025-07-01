package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Student;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends GenericUserService<Student> {
    public StudentService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder, Student.class);
    }
}
