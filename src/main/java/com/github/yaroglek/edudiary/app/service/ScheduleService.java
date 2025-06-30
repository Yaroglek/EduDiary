package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ScheduleDayRepository;
import com.github.yaroglek.edudiary.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

/**
 * Сервис управления расписанием: инициализация на весь учебный год по шаблону,
 * и правки на конкретные дни.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleService {
    private static final LocalDate START_DATE = LocalDate.of(LocalDate.now().getYear(), 9, 1);
    private static final LocalDate END_DATE = LocalDate.of(LocalDate.now().getYear(), 5, 30);

    private final SchoolClassService schoolClassService;
    private final ScheduleDayRepository scheduleDayRepository;
    private final LessonService lessonService;

    public void generateWeeklyLessonsUntilYearEnd(Long classId, DayOfWeek targetDay, List<Lesson> lessonTemplates) {
        if (lessonTemplates == null || lessonTemplates.isEmpty()) {
            throw new IllegalArgumentException("lessonTemplates is null or empty");
        }

        SchoolClass schoolClass = schoolClassService.getById(classId);

        LocalDate startDate = START_DATE;

        // Корректируем startDate до ближайшего нужного дня недели
        while (startDate.getDayOfWeek() != targetDay) {
            startDate = startDate.plusDays(1);
        }

        for (LocalDate date = startDate; !date.isAfter(END_DATE); date = date.plusWeeks(1)) {
            // Проверим, есть ли уже ScheduleDay на эту дату
            LocalDate finalDate = date;
            ScheduleDay scheduleDay = scheduleDayRepository
                    .findByDateAndSchoolClass(date, schoolClass)
                    .orElseGet(() -> {
                        ScheduleDay newDay = ScheduleDay.builder()
                                .date(finalDate)
                                .schoolClass(schoolClass)
                                .build();
                        return scheduleDayRepository.save(newDay);
                    });

            for (Lesson template : lessonTemplates) {
                Lesson lesson = Lesson.builder()
                        .lessonNumber(template.getLessonNumber())
                        .classSubject(template.getClassSubject())
                        .scheduleDay(scheduleDay)
                        .build();

                lessonService.create(lesson);
                log.info("Урок №{} для {} на дату {} создан", lesson.getLessonNumber(), schoolClass.getGrade() + schoolClass.getLiteral(), date);
            }
        }
    }
}

