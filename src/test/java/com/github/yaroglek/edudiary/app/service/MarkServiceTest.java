package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.MarkRepository;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.domain.users.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkServiceTest {

    @Mock
    private MarkRepository markRepository;

    @InjectMocks
    private MarkService markService;

    private Mark mark;

    @BeforeEach
    void setUp() {
        mark = new Mark();
        mark.setId(1L);
        mark.setMarkValue(5);

        Student student = new Student();
        student.setUsername("student1");
        mark.setStudent(student);

        Lesson lesson = new Lesson();
        lesson.setId(10L);
        mark.setLesson(lesson);
    }

    @Test
    void create_shouldSaveMark() {
        when(markRepository.save(mark)).thenReturn(mark);

        Mark saved = markService.create(mark);

        assertNotNull(saved);
        assertEquals(5, saved.getMarkValue());
        verify(markRepository).save(mark);
    }

    @Test
    void create_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () -> markService.create(null));
        verify(markRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnMark() {
        when(markRepository.findById(1L)).thenReturn(Optional.of(mark));

        Mark found = markService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(markRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(markRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> markService.getById(1L));
        verify(markRepository).findById(1L);
    }

    @Test
    void deleteById_shouldCallRepository() {
        markService.deleteById(1L);
        verify(markRepository).deleteById(1L);
    }
}
