package com.github.yaroglek.edudiary.extern.controller.template;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.app.service.MarkService;
import com.github.yaroglek.edudiary.app.service.ScheduleDayService;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.domain.users.Parent;
import com.github.yaroglek.edudiary.domain.users.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/parent")
public class ParentViewController {

    private final UserRepository userRepository;
    private final ScheduleDayService scheduleDayService;
    private final MarkService markService;

    @GetMapping("/schedule/view")
    public String getParentSchedule(
            @RequestParam(value = "selectedStudentId", required = false) Long selectedStudentId,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            Model model,
            Authentication auth) {

        String username = auth.getName();
        Parent parent = (Parent) userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Parent not found"));

        Set<Student> children = parent.getChildren();
        if (children.isEmpty()) {
            model.addAttribute("error", "У вас нет привязанных детей");
            return "parent/schedule";
        }

        Student selectedStudent;
        if (selectedStudentId == null) {
            selectedStudent = children.iterator().next();
            selectedStudentId = selectedStudent.getId();
        } else {
            Long finalSelectedStudentId = selectedStudentId;
            selectedStudent = children.stream()
                    .filter(s -> s.getId().equals(finalSelectedStudentId))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Ребенок не найден или не принадлежит вам"));
        }

        Long classId = selectedStudent.getSchoolClass().getId();

        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(offset);
        LocalDate saturday = monday.plusDays(5);

        List<ScheduleDay> actualDays = scheduleDayService.getByClassAndWeek(classId, monday);

        Map<LocalDate, ScheduleDay> dayMap = actualDays.stream()
                .collect(Collectors.toMap(ScheduleDay::getDate, Function.identity()));

        List<ScheduleDay> fullWeek = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LocalDate day = monday.plusDays(i);
            ScheduleDay dayEntry = dayMap.getOrDefault(day,
                    ScheduleDay.builder()
                            .date(day)
                            .lessons(new ArrayList<>())
                            .schoolClass(selectedStudent.getSchoolClass())
                            .build());

            Map<Integer, Lesson> lessonMap = dayEntry.getLessons().stream()
                    .collect(Collectors.toMap(Lesson::getLessonNumber, Function.identity(), (a, b) -> a));

            List<Lesson> completeLessons = new ArrayList<>();
            for (int lessonNumber = 1; lessonNumber <= 10; lessonNumber++) {
                Lesson lesson = lessonMap.getOrDefault(lessonNumber,
                        Lesson.builder()
                                .lessonNumber(lessonNumber)
                                .scheduleDay(dayEntry)
                                .classSubject(null)
                                .homeworkDescription(null)
                                .marks(Collections.emptySet())
                                .build());
                completeLessons.add(lesson);
            }
            completeLessons.sort(Comparator.comparingInt(Lesson::getLessonNumber));
            dayEntry.setLessons(completeLessons);
            fullWeek.add(dayEntry);
        }

        model.addAttribute("scheduleDays", fullWeek);
        model.addAttribute("allLessons", fullWeek.stream()
                .flatMap(d -> d.getLessons().stream())
                .collect(Collectors.toList()));

        model.addAttribute("currentUsername", selectedStudent.getUsername());
        model.addAttribute("children", children);
        model.addAttribute("selectedStudentId", selectedStudentId);
        model.addAttribute("weekStart", monday);
        model.addAttribute("weekEnd", saturday);
        model.addAttribute("offset", offset);

        return "parent/schedule";
    }

    @GetMapping("/marks/view")
    public String getParentMarks(
            @RequestParam(value = "selectedStudentId", required = false) Long selectedStudentId,
            Model model,
            Authentication auth) {

        String username = auth.getName();
        Parent parent = (Parent) userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Parent not found"));

        Set<Student> children = parent.getChildren();
        if (children.isEmpty()) {
            model.addAttribute("error", "У вас нет привязанных детей");
            return "parent/marks";
        }

        Student selectedStudent;
        if (selectedStudentId == null) {
            selectedStudent = children.iterator().next();
            selectedStudentId = selectedStudent.getId();
        } else {
            Long finalSelectedStudentId = selectedStudentId;
            selectedStudent = children.stream()
                    .filter(s -> s.getId().equals(finalSelectedStudentId))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Ребенок не найден или не принадлежит вам"));
        }

        // Получаем Map<Subject, List<Mark>> для выбранного ребенка
        Map<Subject, List<Mark>> marksBySubject = markService.getMarksGroupedBySubjectSortedByDate(selectedStudent);

        SortedSet<LocalDate> allDates = marksBySubject.values().stream()
                .flatMap(List::stream)
                .map(mark -> mark.getLesson().getScheduleDay().getDate())
                .collect(Collectors.toCollection(TreeSet::new));

        Map<Subject, Map<LocalDate, Mark>> marksBySubjectAndDate = new LinkedHashMap<>();
        for (var entry : marksBySubject.entrySet()) {
            Subject subject = entry.getKey();
            List<Mark> marks = entry.getValue();
            Map<LocalDate, Mark> dateMarkMap = marks.stream()
                    .collect(Collectors.toMap(
                            m -> m.getLesson().getScheduleDay().getDate(),
                            Function.identity(),
                            (existing, replacement) -> existing,
                            LinkedHashMap::new));
            marksBySubjectAndDate.put(subject, dateMarkMap);
        }

        Map<Subject, String> averageMarks = new LinkedHashMap<>();
        for (var entry : marksBySubject.entrySet()) {
            Subject subject = entry.getKey();
            List<Mark> marks = entry.getValue();

            double avg = marks.stream()
                    .mapToInt(Mark::getMarkValue)
                    .average()
                    .orElse(Double.NaN);

            averageMarks.put(subject, Double.isNaN(avg) ? "" : String.format("%.2f", avg));
        }

        model.addAttribute("children", children);
        model.addAttribute("selectedStudentId", selectedStudentId);

        model.addAttribute("marksBySubjectAndDate", marksBySubjectAndDate);
        model.addAttribute("allDates", allDates);
        model.addAttribute("averageMarks", averageMarks);

        return "parent/marks";
    }
}

