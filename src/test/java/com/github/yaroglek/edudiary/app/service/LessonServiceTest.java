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
        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setLessonNumber(2);

        ScheduleDay scheduleDay = new ScheduleDay();
        scheduleDay.setDate(LocalDate.of(2024, 9, 1));
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
}
