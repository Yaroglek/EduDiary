package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ClassSubjectRepository;
import com.github.yaroglek.edudiary.app.service.users.TeacherService;
import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.SchoolClass;
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
public class ClassSubjectService {

    private final ClassSubjectRepository classSubjectRepository;
    private final SchoolClassService schoolClassService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;

    public ClassSubject assignSubjectToClass(Long classId, Long subjectId, Long teacherId) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        Subject subject = subjectService.getById(subjectId);
        Teacher teacher = teacherService.getById(teacherId);

        boolean exists = classSubjectRepository.existsBySchoolClassAndSubject(schoolClass, subject);
        if (exists) {
            log.warn("Связь предмета {} с классом {} уже существует", subject.getName(), classId);
            throw new IllegalStateException("ClassSubject already exists");
        }

        ClassSubject classSubject = ClassSubject.builder()
                .schoolClass(schoolClass)
                .subject(subject)
                .teacher(teacher)
                .build();

        ClassSubject saved = classSubjectRepository.save(classSubject);
        log.info("Связь предмета {} с классом {} и учителем {} создана",
                subject.getName(), schoolClass.getGrade() + schoolClass.getLiteral(), teacher.getUsername());

        return saved;
    }

    public void removeSubjectFromClass(Long classId, Long subjectId) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        Subject subject = subjectService.getById(subjectId);

        ClassSubject cs = classSubjectRepository
                .findBySchoolClassAndSubject(schoolClass, subject)
                .orElseThrow(() -> new NoSuchElementException("ClassSubject not found"));

        log.info("Удаление связи предмета {} с классом {}", subject.getName(), classId);
        classSubjectRepository.delete(cs);
    }

    public ClassSubject updateTeacher(Long classId, Long subjectId, Long newTeacherId) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        Subject subject = subjectService.getById(subjectId);
        Teacher newTeacher = teacherService.getById(newTeacherId);

        ClassSubject classSubject = classSubjectRepository
                .findBySchoolClassAndSubject(schoolClass, subject)
                .orElseThrow(() -> {
                    log.warn("Связь classId={}, subjectId={} не найдена", classId, subjectId);
                    return new NoSuchElementException("ClassSubject not found");
                });

        Teacher oldTeacher = classSubject.getTeacher();
        classSubject.setTeacher(newTeacher);

        log.info("Для класса {} и предмета {} учитель изменён: {} → {}",
                classId + subject.getName(),
                subject.getName(),
                oldTeacher.getUsername(),
                newTeacher.getUsername());

        return classSubject;
    }


    public ClassSubject getById(Long id) {
        ClassSubject cs = classSubjectRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ClassSubject с id={} не найден", id);
                    return new NoSuchElementException("ClassSubject not found");
                });

        log.info("Получена связь ClassSubject id={}", cs.getId());
        return cs;
    }

    public void deleteById(Long id) {
        log.info("Удаление ClassSubject с id={}", id);
        classSubjectRepository.deleteById(id);
    }
}

