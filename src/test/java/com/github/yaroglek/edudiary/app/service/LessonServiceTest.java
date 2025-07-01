package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.LessonRepository;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonService lessonService;

    private Lesson lesson;

    @BeforeEach
    void setUp() {
        lesson = Lesson.builder()
                .id(1L)
                .lessonNumber(2)
                .build();

        ScheduleDay scheduleDay = ScheduleDay.builder()
                .date(LocalDate.of(2024, 9, 1))
                .build();

        lesson.setScheduleDay(scheduleDay);
    }

    @Test
    void create_shouldSaveLesson() {
        when(lessonRepository.save(lesson)).thenReturn(lesson);

        Lesson saved = lessonService.create(lesson);

        assertNotNull(saved);
        assertEquals(2, saved.getLessonNumber());
        verify(lessonRepository).save(lesson);
    }

    @Test
    void create_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () -> lessonService.create(null));
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnLesson() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        Lesson found = lessonService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(lessonRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> lessonService.getById(1L));
        verify(lessonRepository).findById(1L);
    }

    @Test
    void deleteById_shouldCallRepository() {
        lessonService.deleteById(1L);
        verify(lessonRepository).deleteById(1L);
    }

    @Test
    void getLessonsForTeacherInWeek_shouldReturnLessonsInWeek() {
        Long teacherId = 42L;
        LocalDate anyDate = LocalDate.of(2024, 9, 4);

        LocalDate expectedStart = anyDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate expectedEnd = anyDate.with(java.time.DayOfWeek.SUNDAY);

        Lesson lesson1 = Lesson.builder()
                .id(1L)
                .lessonNumber(1)
                .scheduleDay(new ScheduleDay())
                .build();
        lesson1.getScheduleDay().setDate(LocalDate.of(2024, 9, 2));

        Lesson lesson2 = Lesson.builder()
                .id(2L)
                .lessonNumber(2)
                .scheduleDay(new ScheduleDay())
                .build();
        lesson2.getScheduleDay().setDate(LocalDate.of(2024, 9, 5));

        when(lessonRepository.findLessonsForTeacherInWeek(teacherId, expectedStart, expectedEnd))
                .thenReturn(java.util.List.of(lesson1, lesson2));

        var result = lessonService.getLessonsForTeacherInWeek(teacherId, anyDate);

        assertEquals(2, result.size());
        assertTrue(result.contains(lesson1));
        assertTrue(result.contains(lesson2));

        verify(lessonRepository).findLessonsForTeacherInWeek(teacherId, expectedStart, expectedEnd);
    }

}
