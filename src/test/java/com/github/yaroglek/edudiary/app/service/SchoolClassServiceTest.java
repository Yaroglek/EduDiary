package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.SchoolClassRepository;
import com.github.yaroglek.edudiary.app.service.user.StudentService;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import com.github.yaroglek.edudiary.domain.users.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolClassServiceTest {

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private SchoolClassService schoolClassService;

    private SchoolClass schoolClass;
    private Student student;

    @BeforeEach
    void setUp() {
        schoolClass = SchoolClass.builder()
                .id(1L)
                .grade(5)
                .literal("А")
                .students(new HashSet<>())
                .build();

        student = new Student();
        student.setId(2L);
        student.setUsername("student1");
    }

    @Test
    void create_shouldSaveSchoolClass() {
        when(schoolClassRepository.save(schoolClass)).thenReturn(schoolClass);

        SchoolClass saved = schoolClassService.create(schoolClass);

        assertNotNull(saved);
        assertEquals(5, saved.getGrade());
        verify(schoolClassRepository).save(schoolClass);
    }

    @Test
    void create_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () -> schoolClassService.create(null));
        verifyNoInteractions(schoolClassRepository);
    }

    @Test
    void getById_shouldReturnSchoolClass() {
        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));

        SchoolClass found = schoolClassService.getById(1L);

        assertNotNull(found);
        assertEquals("А", found.getLiteral());
        verify(schoolClassRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(schoolClassRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> schoolClassService.getById(1L));
        verify(schoolClassRepository).findById(1L);
    }

    @Test
    void deleteById_shouldCallRepository() {
        schoolClassService.deleteById(1L);
        verify(schoolClassRepository).deleteById(1L);
    }

    @Test
    void addStudent_shouldAssignStudentToClass() {
        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));
        when(studentService.getById(2L)).thenReturn(student);

        schoolClassService.addStudent(1L, 2L);

        assertEquals(schoolClass, student.getSchoolClass());
        verify(schoolClassRepository).findById(1L);
        verify(studentService).getById(2L);
    }

    @Test
    void addStudent_shouldSkipIfAlreadyAssigned() {
        student.setSchoolClass(schoolClass);

        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));
        when(studentService.getById(2L)).thenReturn(student);

        schoolClassService.addStudent(1L, 2L);

        assertEquals(schoolClass, student.getSchoolClass());
        verify(schoolClassRepository).findById(1L);
        verify(studentService).getById(2L);
    }

    @Test
    void removeStudent_shouldUnsetSchoolClass() {
        student.setSchoolClass(schoolClass);
        schoolClass.getStudents().add(student); // ВАЖНО!

        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));
        when(studentService.getById(2L)).thenReturn(student);

        schoolClassService.removeStudent(1L, 2L);

        assertNull(student.getSchoolClass());
        verify(schoolClassRepository).findById(1L);
        verify(studentService).getById(2L);
    }


    @Test
    void removeStudent_shouldSkipIfNotInClass() {
        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));
        when(studentService.getById(2L)).thenReturn(student);

        schoolClassService.removeStudent(1L, 2L);

        assertNull(student.getSchoolClass());
        verify(schoolClassRepository).findById(1L);
        verify(studentService).getById(2L);
    }
}
