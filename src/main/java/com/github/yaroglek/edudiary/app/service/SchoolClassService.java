package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.service.user.StudentService;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import com.github.yaroglek.edudiary.app.repository.SchoolClassRepository;
import com.github.yaroglek.edudiary.domain.users.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SchoolClassService {
    private final SchoolClassRepository schoolClassRepository;
    private final StudentService studentService;

    public SchoolClass create(SchoolClass schoolClass) {
        if (schoolClass == null) {
            throw new IllegalArgumentException("SchoolClass is null");
        }

        SchoolClass saved = schoolClassRepository.save(schoolClass);
        log.info("Создан школьный класс: {}", saved.getGrade() + saved.getLiteral());
        return saved;
    }

    public SchoolClass getById(Long id) {
        SchoolClass schoolClass = schoolClassRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Класс с id={} не найден", id);
                    return new NoSuchElementException("SchoolClass not found");
                });

        log.info("Получен школьный класс: {}", schoolClass.getGrade() + schoolClass.getLiteral());
        return schoolClass;
    }

    public void deleteById(Long id) {
        log.info("Удаление школьного класса с id={}", id);
        schoolClassRepository.deleteById(id);
    }

    public void addStudent(Long classId, Long studentId) {
        SchoolClass schoolClass = getById(classId);
        Student student = studentService.getById(studentId);

        if (student.getSchoolClass() != null && student.getSchoolClass().getId().equals(classId)) {
            log.warn("Ученик {} уже принадлежит классу {}", student.getUsername(), classId);
            return;
        }

        student.setSchoolClass(schoolClass);
        log.info("Ученик {} добавлен в класс {}", student.getUsername(), classId);
    }

    public void removeStudent(Long classId, Long studentId) {
        SchoolClass schoolClass = getById(classId);
        Student student = studentService.getById(studentId);

        if (!schoolClass.getStudents().contains(student)) {
            log.warn("Ученик {} не состоит в классе {}", student.getUsername(), classId);
            return;
        }

        student.setSchoolClass(null);
        log.info("Ученик {} удалён из класса {}", student.getUsername(), classId);
    }
}

