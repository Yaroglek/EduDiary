package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.SubjectRepository;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.domain.users.Teacher;
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
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject subject;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Teacher teacher = new Teacher();
        teacher.setId(10L);
        teacher.setUsername("teacher1");
    }

    @Test
    void create_shouldSaveSubject() {
        when(subjectRepository.save(subject)).thenReturn(subject);

        Subject saved = subjectService.create(subject);

        assertNotNull(saved);
        assertEquals("Math", saved.getName());
        verify(subjectRepository).save(subject);
    }

    @Test
    void create_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () -> subjectService.create(null));
        verify(subjectRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnSubject() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        Subject found = subjectService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(subjectRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> subjectService.getById(1L));
        verify(subjectRepository).findById(1L);
    }

    @Test
    void deleteById_shouldCallRepository() {
        subjectService.deleteById(1L);
        verify(subjectRepository).deleteById(1L);
    }
}
