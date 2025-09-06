package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.Parent;
import com.github.yaroglek.edudiary.domain.users.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParentService extends GenericUserService<Parent> {
    private final StudentService studentService;

    public ParentService(UserRepository userRepository, PasswordEncoder passwordEncoder, StudentService studentService) {
        super(userRepository, passwordEncoder, Parent.class);
        this.studentService = studentService;
    }

    /**
     * Метод для привязки ребенка к родителю.
     *
     * @param parentId  - ID родителя
     * @param studentId - ID ребенка
     */
    public void addChild(Long parentId, Long studentId) {
        Parent parent = getById(parentId);
        Student student = studentService.getById(studentId);

        if (!parent.getChildren().contains(student)) {
            parent.getChildren().add(student);
            userRepository.save(parent);
            log.info("Student {} added to parent {}", student.getUsername(), parentId);
        } else {
            log.warn("Student {} is already a child of parent {}", student.getUsername(), parentId);
        }
    }

    /**
     * Метод для отвязки ребенка от родителя
     *
     * @param parentId  - ID родителя
     * @param studentId - ID ребенка
     */
    public void removeChild(Long parentId, Long studentId) {
        Parent parent = getById(parentId);
        Student student = studentService.getById(studentId);

        if (parent.getChildren().contains(student)) {
            parent.getChildren().remove(student);
            userRepository.save(parent);
            log.info("Student {} removed from parent {}", student.getUsername(), parentId);
        } else {
            log.warn("Student {} is not a child of parent {}", student.getUsername(), parentId);
        }
    }
}

