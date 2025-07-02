package com.github.yaroglek.edudiary.extern.controller.template;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.app.service.LessonService;
import com.github.yaroglek.edudiary.app.service.MarkService;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import com.github.yaroglek.edudiary.domain.users.Student;
import com.github.yaroglek.edudiary.domain.users.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherViewController {

    private final UserRepository userRepository;
    private final LessonService lessonService;
    private final MarkService markService;

    @GetMapping("/schedule/view")
    public String getTeacherSchedule(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                     Model model, Authentication auth) {
        String username = auth.getName();
        Teacher teacher = (Teacher) userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));

        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY).plusWeeks(offset);
        LocalDate saturday = monday.plusDays(5);

        List<Lesson> lessons = lessonService.getLessonsForTeacherInWeek(teacher.getId(), monday);

        Map<LocalDate, List<Lesson>> lessonsByDate = lessons.stream()
                .collect(Collectors.groupingBy(l -> l.getScheduleDay().getDate()));

        List<ScheduleDay> fullWeek = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            LocalDate currentDay = monday.plusDays(i);
            List<Lesson> dayLessons = lessonsByDate.getOrDefault(currentDay, new ArrayList<>());

            Map<Integer, Lesson> lessonMap = dayLessons.stream()
                    .collect(Collectors.toMap(Lesson::getLessonNumber, Function.identity(), (a, b) -> a));

            List<Lesson> completeLessons = new ArrayList<>();
            for (int lessonNumber = 1; lessonNumber <= 10; lessonNumber++) {
                Lesson lesson = lessonMap.getOrDefault(lessonNumber,
                        Lesson.builder()
                                .lessonNumber(lessonNumber)
                                .scheduleDay(ScheduleDay.builder().date(currentDay).build())
                                .classSubject(null)
                                .homeworkDescription(null)
                                .marks(Collections.emptySet())
                                .build());
                completeLessons.add(lesson);
            }

            completeLessons.sort(Comparator.comparingInt(Lesson::getLessonNumber));

            fullWeek.add(ScheduleDay.builder()
                    .date(currentDay)
                    .lessons(completeLessons)
                    .build());
        }

        model.addAttribute("scheduleDays", fullWeek);
        model.addAttribute("allLessons", fullWeek.stream()
                .flatMap(d -> d.getLessons().stream())
                .collect(Collectors.toList()));

        model.addAttribute("weekStart", monday);
        model.addAttribute("weekEnd", saturday);
        model.addAttribute("offset", offset);

        return "teacher/schedule";
    }

    /**
     * Страница редактирования урока
     *
     * @param id    ID урока
     * @param model модель
     * @return шаблон редактирования
     */
    @GetMapping("/lesson/{id}/view")
    public String editLesson(@PathVariable Long id, Model model) {
        Lesson lesson = lessonService.getById(id);

        Long classId = lesson.getClassSubject().getSchoolClass().getId();
        Map<Student, Mark> markMap = markService.getMarksForLessonAndClass(id, classId);

        List<StudentMarkView> studentMarks = markMap.entrySet().stream()
                .map(entry -> new StudentMarkView(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(sm -> sm.student().getFullName()))
                .toList();

        model.addAttribute("lesson", lesson);
        model.addAttribute("studentMarks", studentMarks);

        return "teacher/lesson";
    }

    @PostMapping("/lessons/{id}/homework")
    public String updateHomework(@PathVariable Long id,
                                 @RequestParam("homeworkDescription") String homeworkDescription) {
        Lesson lesson = new Lesson();
        lesson.setHomeworkDescription(homeworkDescription);
        lessonService.update(id, lesson);

        return "redirect:/teacher/lesson/" + id + "/view";
    }

    @PostMapping("/lessons/{id}/marks")
    public String updateMarks(@PathVariable Long id,
                              @RequestParam Map<String, String> formData) {
        Lesson lesson = lessonService.getById(id);
        Long classId = lesson.getClassSubject().getSchoolClass().getId();

        Map<Student, Mark> existingMarks = markService.getMarksForLessonAndClass(id, classId);

        for (Map.Entry<String, String> entry : formData.entrySet()) {
            String key = entry.getKey();

            if (!key.startsWith("marks[")) continue;

            // Пример ключей: marks[3].value, marks[3].comment
            String studentIdStr = key.substring("marks[".length(), key.indexOf(']'));
            Long studentId = Long.parseLong(studentIdStr);

            Student student = existingMarks.keySet().stream()
                    .filter(s -> s.getId().equals(studentId))
                    .findFirst()
                    .orElse(null);
            if (student == null) continue;

            String valueKey = "marks[" + studentId + "].value";
            String commentKey = "marks[" + studentId + "].comment";

            String valueStr = formData.get(valueKey);
            String comment = formData.getOrDefault(commentKey, "").trim();

            if (valueStr == null || valueStr.isBlank()) continue;

            Integer markValue;
            try {
                markValue = Integer.parseInt(valueStr);
            } catch (NumberFormatException e) {
                continue;
            }

            Mark existing = existingMarks.get(student);

            if (existing == null) {
                // создаём новую
                Mark newMark = Mark.builder()
                        .student(student)
                        .lesson(lesson)
                        .markValue(markValue)
                        .comment(comment)
                        .build();
                existingMarks.put(student, newMark);
                markService.create(newMark);
            } else {
                // обновляем существующую
                Mark updated = new Mark();
                updated.setMarkValue(markValue);
                updated.setComment(comment);
                markService.update(existing.getId(), updated);
            }
        }

        return "redirect:/teacher/lesson/" + id + "/view";
    }

        public record StudentMarkView(Student student, Mark mark) {}
}

