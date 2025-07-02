package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.service.user.StudentService;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import com.github.yaroglek.edudiary.app.repository.SchoolClassRepository;
import com.github.yaroglek.edudiary.domain.users.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final StudentService studentService;

    /**
     * Метод для создания школьного класса
     *
     * @param schoolClass - объект школьного класса
     * @return - сохранённый школьный класс
     */
    public SchoolClass create(SchoolClass schoolClass) {
        if (schoolClass == null) {
            throw new IllegalArgumentException("SchoolClass is null");
        }

        SchoolClass saved = schoolClassRepository.save(schoolClass);
        log.info("SchoolClass created: {}{}", saved.getGrade(), saved.getLiteral());
        return saved;
    }

    /**
     * Метод для получения школьного класса по ID
     *
     * @param id - ID класса
     * @return - найденный класс
     */
    public SchoolClass getById(Long id) {
        SchoolClass schoolClass = schoolClassRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("SchoolClass with id {} not found", id);
                    return new NoSuchElementException("SchoolClass not found");
                });

        log.info("SchoolClass with id {} found: {}{}", id, schoolClass.getGrade(), schoolClass.getLiteral());
        return schoolClass;
    }

    /**
     * Метод для обновления школьного класса. Обновляются только год и буква.
     *
     * @param id           - ID класса
     * @param updatedClass - обновленный класс
     * @return - сохраненный класс
     */
    public SchoolClass update(Long id, SchoolClass updatedClass) {
        if (updatedClass == null) {
            throw new IllegalArgumentException("SchoolClass is null");
        }

        SchoolClass existingClass = getById(id);

        existingClass.setGrade(updatedClass.getGrade());
        existingClass.setLiteral(updatedClass.getLiteral());

        SchoolClass saved = schoolClassRepository.save(existingClass);
        log.info("SchoolClass with id {} updated: grade={}, literal={}", saved.getId(), saved.getGrade(), saved.getLiteral());
        return saved;
    }

    /**
     * Метод для удаления школьного класса по ID
     *
     * @param id - ID класса
     */
    public void deleteById(Long id) {
        log.info("SchoolClass with id {} deleted", id);
        schoolClassRepository.deleteById(id);
    }

    /**
     * Метод для добавления ученика в школьный класс
     *
     * @param classId   - ID класса
     * @param studentId - ID ученика
     */
    public void addStudent(Long classId, Long studentId) {
        SchoolClass schoolClass = getById(classId);
        Student student = studentService.getById(studentId);

        if (!schoolClass.getStudents().contains(student)) {
            schoolClass.getStudents().add(student);
            student.setSchoolClass(schoolClass);
            log.info("Student {} added to class {}", student.getUsername(), classId);
        } else {
            log.warn("Student {} is already in class {}", student.getUsername(), classId);
        }
    }

    /**
     * Метод для удаления ученика из школьного класса
     *
     * @param classId   - ID класса
     * @param studentId - ID ученика
     */
    public void removeStudent(Long classId, Long studentId) {
        SchoolClass schoolClass = getById(classId);
        Student student = studentService.getById(studentId);

        if (schoolClass.getStudents().contains(student)) {
            schoolClass.getStudents().remove(student);
            student.setSchoolClass(null);
            log.info("Student {} removed from class {}", student.getUsername(), classId);
        } else {
            log.warn("Student {} is not part of class {}", student.getUsername(), classId);
        }
    }

    /**
     * Метод для получения всех классов
     *
     * @return - список классов
     */
    public List<SchoolClass> getAll() {
        log.info("Get all classes");

        return schoolClassRepository.findAll();
    }
}

