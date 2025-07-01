package com.github.yaroglek.edudiary.app.service.user;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
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
class StudentServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setUsername("student1");
    }

    @Test
    void create_shouldSaveStudent() {
        when(userRepository.save(student)).thenReturn(student);

        Student saved = studentService.create(student);

        assertNotNull(saved);
        assertEquals("student1", saved.getUsername());
        verify(userRepository, times(1)).save(student);
    }

    @Test
    void create_saveNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.create(null));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnStudent() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));

        Student found = studentService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> studentService.getById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById_shouldInvokeRepository() {
        studentService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
