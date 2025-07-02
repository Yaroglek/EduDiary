package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.MarkRepository;
import com.github.yaroglek.edudiary.app.service.user.StudentService;
import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.domain.users.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;
    private final SchoolClassService schoolClassService;
    private final StudentService studentService;

    /**
     * Метод для сохранения новой оценки
     *
     * @param mark - оценка для сохранения
     * @return - сохраненная оценка
     */
    public Mark create(Mark mark) {
        if (mark == null) {
            throw new IllegalArgumentException("Mark is null");
        }

        Mark saved = markRepository.save(mark);
        log.info("Mark with value {} created for student {} and lesson {}",
                saved.getMarkValue(), saved.getStudent().getUsername(), saved.getLesson().getId());
        return saved;
    }

    /**
     * Метод для получения оценки по ID
     *
     * @param id - ID оценки
     * @return - найденная оценка
     */
    public Mark getById(Long id) {
        Mark mark = markRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mark with id {} not found", id);
                    return new NoSuchElementException("Mark not found");
                });

        log.info("Mark with id {} found with value {}", id, mark.getMarkValue());
        return mark;
    }

    /**
     * Метод для обновления оценки. Меняется только значение и комментарий.
     *
     * @param id          - ID оценки
     * @param updatedMark - обновленная оценка
     * @return - сохраненная оценка
     */
    public Mark update(Long id, Mark updatedMark) {
        if (updatedMark == null) {
            throw new IllegalArgumentException("Mark is null");
        }

        Mark existingMark = getById(id);

        existingMark.setMarkValue(updatedMark.getMarkValue());
        existingMark.setComment(updatedMark.getComment());

        Mark saved = markRepository.save(existingMark);
        log.info("Mark with id {} updated", saved.getId());
        return saved;
    }

    /**
     * Метод для удаления оценки по ID
     *
     * @param id - ID оценки
     */
    public void deleteById(Long id) {
        log.info("Mark with id {} deleted", id);
        markRepository.deleteById(id);
    }

    /**
     * Метод для получения словаря ученик → оценка для заданного урока и класса.
     *
     * @param lessonId - ID урока
     * @param classId  - ID класса
     * @return - словарь, где ключ - ученик, значение - его оценка (может быть null)
     */
    public Map<Student, Mark> getMarksForLessonAndClass(Long lessonId, Long classId) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        Set<Student> students = schoolClass.getStudents();

        List<Mark> marks = markRepository.findAllByLessonId(lessonId);

        Map<Long, Mark> markByStudentId = marks.stream()
                .filter(m -> m.getStudent() != null)
                .collect(Collectors.toMap(m -> m.getStudent().getId(), Function.identity()));

        Map<Student, Mark> result = new HashMap<>();
        for (Student student : students) {
            result.put(student, markByStudentId.get(student.getId()));
        }

        return result;
    }

    /**
     * Метод для получения всех оценок у ученика,
     * сгруппированных по предметам и отсортированных по возрастанию даты урока.
     * Даже если нет ни одной оценки по этому предмету, все равно он будет добавлен в словарь
     *
     * @param student - ученик
     * @return - все оценки ученика оп предметам
     */
    public Map<Subject, List<Mark>> getMarksGroupedBySubjectSortedByDate(Student student) {
        List<Subject> allSubjects = student.getSchoolClass().getClassSubjects().stream()
                .map(ClassSubject::getSubject)
                .toList();

        Map<Subject, List<Mark>> marksBySubject = student.getMarks().stream()
                .sorted(Comparator.comparing(mark -> mark.getLesson().getScheduleDay().getDate()))
                .collect(Collectors.groupingBy(
                        mark -> mark.getLesson().getClassSubject().getSubject(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        Map<Subject, List<Mark>> result = new LinkedHashMap<>();

        for (Subject subject : allSubjects) {
            result.put(subject, marksBySubject.getOrDefault(subject, Collections.emptyList()));
        }

        return result;
    }


}

