package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
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
class TeacherServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUsername("teacher1");
    }

    @Test
    void create_shouldSaveTeacher() {
        when(userRepository.save(teacher)).thenReturn(teacher);

        Teacher saved = teacherService.create(teacher);

        assertNotNull(saved);
        assertEquals("teacher1", saved.getUsername());
        verify(userRepository, times(1)).save(teacher);
    }

    @Test
    void create_saveNull() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.create(null));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnTeacher() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher found = teacherService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> teacherService.getById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById_shouldInvokeRepository() {
        teacherService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
