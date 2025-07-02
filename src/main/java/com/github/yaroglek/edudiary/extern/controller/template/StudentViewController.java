package com.github.yaroglek.edudiary.extern.controller.template;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.app.service.MarkService;
import com.github.yaroglek.edudiary.app.service.ScheduleDayService;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import com.github.yaroglek.edudiary.domain.Subject;
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
@RequestMapping("/student")
public class StudentViewController {

    private final UserRepository userRepository;
    private final ScheduleDayService scheduleDayService;
    private final MarkService markService;

    @GetMapping("/schedule/view")
    public String getStudentSchedule(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                     Model model, Authentication auth) {
        String username = auth.getName();
        Student student = (Student) userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));
        Long classId = student.getSchoolClass().getId();

        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY).plusWeeks(offset);
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
                            .schoolClass(student.getSchoolClass())
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
        model.addAttribute("currentUsername", username);

        model.addAttribute("weekStart", monday);
        model.addAttribute("weekEnd", saturday);
        model.addAttribute("offset", offset);

        return "student/schedule";
    }

    @GetMapping("/marks/view")
    public String getStudentMarks(Model model, Authentication auth) {
        String username = auth.getName();
        Student student = (Student) userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));

        Map<Subject, List<Mark>> marksBySubject = markService.getMarksGroupedBySubjectSortedByDate(student);

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

        model.addAttribute("marksBySubjectAndDate", marksBySubjectAndDate);
        model.addAttribute("allDates", allDates);
        model.addAttribute("averageMarks", averageMarks);

        return "student/marks";
    }
}


