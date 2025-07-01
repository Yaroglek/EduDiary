package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ClassSubjectRepository;
import com.github.yaroglek.edudiary.app.service.user.TeacherService;
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

    /**
     * Метод для назначения предмета определённому классу с указанным учителем
     *
     * @param classId   ID класса
     * @param subjectId ID предмета
     * @param teacherId ID учителя
     * @return созданная связь ClassSubject
     */
    public ClassSubject assignSubjectToClass(Long classId, Long subjectId, Long teacherId) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        Subject subject = subjectService.getById(subjectId);
        Teacher teacher = teacherService.getById(teacherId);

        boolean exists = classSubjectRepository.existsBySchoolClassAndSubject(schoolClass, subject);
        if (exists) {
            log.warn("ClassSubject already exists for subject {} and class {}", subject.getName(), classId);
            throw new IllegalStateException("ClassSubject already exists");
        }

        ClassSubject classSubject = ClassSubject.builder()
                .schoolClass(schoolClass)
                .subject(subject)
                .teacher(teacher)
                .build();

        ClassSubject saved = classSubjectRepository.save(classSubject);
        log.info("ClassSubject created: subject {} for class {} with teacher {}",
                subject.getName(), schoolClass.getGrade() + schoolClass.getLiteral(), teacher.getUsername());

        return saved;
    }

    /**
     * Метод для удаления связи предмета с классом
     *
     * @param classId   ID класса
     * @param subjectId ID предмета
     */
    public void removeSubjectFromClass(Long classId, Long subjectId) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        Subject subject = subjectService.getById(subjectId);

        ClassSubject cs = classSubjectRepository.findBySchoolClassAndSubject(schoolClass, subject)
                .orElseThrow(() -> {
                    log.warn("ClassSubject not found for classId {} and subjectId {}", classId, subjectId);
                    return new NoSuchElementException("ClassSubject not found");
                });

        classSubjectRepository.delete(cs);
        log.info("ClassSubject deleted: subject {} removed from class {}", subject.getName(), classId);
    }

    /**
     * Метод для обновления учителя для предмета в конкретном классе
     *
     * @param classId      ID класса
     * @param subjectId    ID предмета
     * @param newTeacherId ID нового учителя
     * @return обновлённый объект ClassSubject
     */
    public ClassSubject updateTeacher(Long classId, Long subjectId, Long newTeacherId) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        Subject subject = subjectService.getById(subjectId);
        Teacher newTeacher = teacherService.getById(newTeacherId);

        ClassSubject classSubject = classSubjectRepository.findBySchoolClassAndSubject(schoolClass, subject)
                .orElseThrow(() -> {
                    log.warn("ClassSubject not found for classId {} and subjectId {}", classId, subjectId);
                    return new NoSuchElementException("ClassSubject not found");
                });

        Teacher oldTeacher = classSubject.getTeacher();
        classSubject.setTeacher(newTeacher);

        log.info("Teacher updated for subject {} in class {}: {} -> {}",
                subject.getName(), classId, oldTeacher.getUsername(), newTeacher.getUsername());

        return classSubject;
    }

    /**
     * Метод для получения связи ClassSubject по ID
     *
     * @param id - ID ClassSubject
     * @return найденный ClassSubject
     */
    public ClassSubject getById(Long id) {
        ClassSubject cs = classSubjectRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ClassSubject with id {} not found", id);
                    return new NoSuchElementException("ClassSubject not found");
                });

        log.info("ClassSubject with id {} found", cs.getId());
        return cs;
    }

    /**
     * Метод для удаления ClassSubject по ID
     *
     * @param id - ID ClassSubject
     */
    public void deleteById(Long id) {
        log.info("ClassSubject with id {} deleted", id);
        classSubjectRepository.deleteById(id);
    }
}
