package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.SubjectRepository;
import com.github.yaroglek.edudiary.app.service.user.TeacherService;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.domain.users.Teacher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final TeacherService teacherService;

    /**
     * Метод для создания предмета
     *
     * @param subject - предмет для сохранения
     * @return - сохранённый предмет
     */
    public Subject create(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject is null");
        }

        Subject saved = subjectRepository.save(subject);
        log.info("Subject created: {}", saved.getName());
        return saved;
    }

    /**
     * Метод для получения предмета по ID
     *
     * @param id - ID предмета
     * @return - найденный предмет
     */
    public Subject getById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Subject with id {} not found", id);
                    return new NoSuchElementException("Subject not found");
                });

        log.info("Subject with id {} found: {}", id, subject.getName());
        return subject;
    }

    /**
     * Метод для удаления предмета по ID
     *
     * @param id - ID предмета
     */
    public void deleteById(Long id) {
        log.info("Subject with id {} deleted", id);
        subjectRepository.deleteById(id);
    }

    /**
     * Метод для привязки учителя к предмету
     *
     * @param subjectId - ID предмета
     * @param teacherId - ID учителя
     */
    public void assignTeacher(Long subjectId, Long teacherId) {
        Subject subject = getById(subjectId);
        Teacher teacher = teacherService.getById(teacherId);

        if (!subject.getTeachers().contains(teacher)) {
            subject.getTeachers().add(teacher);
            teacher.getSubjects().add(subject);
            log.info("Teacher {} assigned to subject {}", teacher.getUsername(), subject.getName());
        } else {
            log.warn("Teacher {} is already assigned to subject {}", teacher.getUsername(), subject.getName());
        }
    }

    /**
     * Метод для отвязки учителя от предмета
     *
     * @param subjectId - ID предмета
     * @param teacherId - ID учителя
     */
    public void removeTeacher(Long subjectId, Long teacherId) {
        Subject subject = getById(subjectId);
        Teacher teacher = teacherService.getById(teacherId);

        if (subject.getTeachers().contains(teacher)) {
            subject.getTeachers().remove(teacher);
            teacher.getSubjects().remove(subject);
            log.info("Teacher {} unassigned from subject {}", teacher.getUsername(), subject.getName());
        } else {
            log.warn("Teacher {} was not assigned to subject {}", teacher.getUsername(), subject.getName());
        }
    }
}