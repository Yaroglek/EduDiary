package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ScheduleDayRepository;
import com.github.yaroglek.edudiary.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleService {

    private static final LocalDate START_DATE = LocalDate.of(LocalDate.now().getYear(), 9, 1);
    private static final LocalDate END_DATE = LocalDate.of(LocalDate.now().plusYears(1).getYear(), 5, 30);

    private final SchoolClassService schoolClassService;
    private final ScheduleDayRepository scheduleDayRepository;
    private final LessonService lessonService;

    /**
     * Метод для генерации расписания на каждый указанный день недели до конца учебного года.
     *
     * @param classId         ID класса
     * @param targetDay       день недели, в который нужно проводить уроки
     * @param lessonTemplates список шаблонов уроков
     */
    public void generateWeeklyLessonsUntilYearEnd(Long classId, DayOfWeek targetDay, List<Lesson> lessonTemplates) {
        if (lessonTemplates == null || lessonTemplates.isEmpty()) {
            throw new IllegalArgumentException("lessonTemplates is null or empty");
        }

        SchoolClass schoolClass = schoolClassService.getById(classId);
        LocalDate startDate = START_DATE;

        while (startDate.getDayOfWeek() != targetDay) {
            startDate = startDate.plusDays(1);
        }

        for (LocalDate date = startDate; !date.isAfter(END_DATE); date = date.plusWeeks(1)) {
            LocalDate finalDate = date;
            ScheduleDay scheduleDay = scheduleDayRepository.findByDateAndSchoolClass(date, schoolClass)
                    .orElseGet(() -> scheduleDayRepository.save(ScheduleDay.builder()
                            .date(finalDate)
                            .schoolClass(schoolClass)
                            .build()));

            for (Lesson template : lessonTemplates) {
                Lesson lesson = Lesson.builder()
                        .lessonNumber(template.getLessonNumber())
                        .classSubject(template.getClassSubject())
                        .scheduleDay(scheduleDay)
                        .build();

                lessonService.create(lesson);
                log.info("Lesson #{} created for class {} on {}", lesson.getLessonNumber(), schoolClass.getGrade() + schoolClass.getLiteral(), date);
            }
        }
    }
}

