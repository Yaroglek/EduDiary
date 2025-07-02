package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.domain.users.Teacher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService extends GenericUserService<Teacher> {
    public TeacherService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder, Teacher.class);
    }

    /**
     * Метод для нахождения учителей, которые не назначены на заданный предмет.
     *
     * @param subject - предмет.
     * @return - список учителей.
     */
    public List<Teacher> findTeachersNotAssignedToSubject(Subject subject) {
        return getAll().stream()
                .filter(teacher -> teacher.getSubjects() == null || !teacher.getSubjects().contains(subject))
                .collect(Collectors.toList());
    }
}