package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ScheduleDayRepository;
import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private SchoolClassService schoolClassService;

    @Mock
    private ScheduleDayRepository scheduleDayRepository;

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private ScheduleService scheduleService;

    private SchoolClass schoolClass;
    private Lesson templateLesson;

    @BeforeEach
    void setUp() {
        schoolClass = SchoolClass.builder()
                .id(1L)
                .grade(5)
                .literal("A")
                .build();

        ClassSubject classSubject = ClassSubject.builder().id(1L).build();

        templateLesson = Lesson.builder()
                .lessonNumber(1)
                .classSubject(classSubject)
                .build();
    }

    @Test
    void generateWeeklyLessonsUntilYearEnd_shouldGenerateLessons() {
        List<Lesson> templates = List.of(templateLesson);
        DayOfWeek targetDay = DayOfWeek.MONDAY;

        when(schoolClassService.getById(1L)).thenReturn(schoolClass);
        lenient().when(scheduleDayRepository.findByDateAndSchoolClass(any(), any()))
                .thenReturn(Optional.empty());
        lenient().when(scheduleDayRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));


        scheduleService.generateWeeklyLessonsUntilYearEnd(1L, targetDay, templates);

        long count = countMondaysBetween(
                LocalDate.of(LocalDate.now().getYear(), 9, 1),
                LocalDate.of(LocalDate.now().plusYears(1).getYear(), 5, 30)
        );

        verify(scheduleDayRepository, times((int) count)).save(any());
        verify(lessonService, times((int) count * templates.size())).create(any());
    }

    @Test
    void generateWeeklyLessonsUntilYearEnd_shouldThrowIfTemplatesNull() {
        assertThrows(IllegalArgumentException.class, () ->
                scheduleService.generateWeeklyLessonsUntilYearEnd(1L, DayOfWeek.MONDAY, null));
    }

    @Test
    void generateWeeklyLessonsUntilYearEnd_shouldThrowIfTemplatesEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                scheduleService.generateWeeklyLessonsUntilYearEnd(1L, DayOfWeek.MONDAY, List.of()));
    }

    private long countMondaysBetween(LocalDate start, LocalDate end) {
        long count = 0;
        LocalDate date = start;
        while (date.getDayOfWeek() != DayOfWeek.MONDAY) {
            date = date.plusDays(1);
        }
        while (!date.isAfter(end)) {
            count++;
            date = date.plusWeeks(1);
        }
        return count;
    }
}
