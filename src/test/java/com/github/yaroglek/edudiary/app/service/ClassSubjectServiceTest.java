package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ClassSubjectRepository;
import com.github.yaroglek.edudiary.app.service.user.TeacherService;
import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.SchoolClass;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassSubjectServiceTest {

    @Mock
    private ClassSubjectRepository classSubjectRepository;

    @Mock
    private SchoolClassService schoolClassService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private ClassSubjectService classSubjectService;

    private SchoolClass schoolClass;
    private Subject subject;
    private Teacher teacher;
    private ClassSubject classSubject;

    @BeforeEach
    void setUp() {
        schoolClass = new SchoolClass();
        schoolClass.setId(1L);

        subject = new Subject();
        subject.setId(2L);
        subject.setName("Math");

        teacher = new Teacher();
        teacher.setId(3L);
        teacher.setUsername("teacher1");

        classSubject = ClassSubject.builder()
                .id(10L)
                .schoolClass(schoolClass)
                .subject(subject)
                .teacher(teacher)
                .build();
    }

    @Test
    void assignSubjectToClass_shouldCreateClassSubject() {
        when(schoolClassService.getById(1L)).thenReturn(schoolClass);
        when(subjectService.getById(2L)).thenReturn(subject);
        when(teacherService.getById(3L)).thenReturn(teacher);
        when(classSubjectRepository.existsBySchoolClassAndSubject(schoolClass, subject)).thenReturn(false);
        when(classSubjectRepository.save(any())).thenReturn(classSubject);

        ClassSubject result = classSubjectService.assignSubjectToClass(1L, 2L, 3L);

        assertNotNull(result);
        assertEquals(schoolClass, result.getSchoolClass());
        assertEquals(subject, result.getSubject());
        assertEquals(teacher, result.getTeacher());
    }

    @Test
    void assignSubjectToClass_shouldThrowIfExists() {
        when(schoolClassService.getById(1L)).thenReturn(schoolClass);
        when(subjectService.getById(2L)).thenReturn(subject);
        when(teacherService.getById(3L)).thenReturn(teacher);
        when(classSubjectRepository.existsBySchoolClassAndSubject(schoolClass, subject)).thenReturn(true);

        assertThrows(IllegalStateException.class, () ->
                classSubjectService.assignSubjectToClass(1L, 2L, 3L));
    }

    @Test
    void removeSubjectFromClass_shouldDelete() {
        when(schoolClassService.getById(1L)).thenReturn(schoolClass);
        when(subjectService.getById(2L)).thenReturn(subject);
        when(classSubjectRepository.findBySchoolClassAndSubject(schoolClass, subject))
                .thenReturn(Optional.of(classSubject));

        classSubjectService.removeSubjectFromClass(1L, 2L);

        verify(classSubjectRepository).delete(classSubject);
    }

    @Test
    void removeSubjectFromClass_shouldThrowIfNotFound() {
        when(schoolClassService.getById(1L)).thenReturn(schoolClass);
        when(subjectService.getById(2L)).thenReturn(subject);
        when(classSubjectRepository.findBySchoolClassAndSubject(schoolClass, subject))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                classSubjectService.removeSubjectFromClass(1L, 2L));
    }

    @Test
    void updateTeacher_shouldUpdateTeacher() {
        Teacher newTeacher = new Teacher();
        newTeacher.setId(4L);
        newTeacher.setUsername("teacher2");

        when(schoolClassService.getById(1L)).thenReturn(schoolClass);
        when(subjectService.getById(2L)).thenReturn(subject);
        when(teacherService.getById(4L)).thenReturn(newTeacher);
        when(classSubjectRepository.findBySchoolClassAndSubject(schoolClass, subject))
                .thenReturn(Optional.of(classSubject));

        ClassSubject result = classSubjectService.updateTeacher(1L, 2L, 4L);

        assertEquals(newTeacher, result.getTeacher());
    }

    @Test
    void updateTeacher_shouldThrowIfNotFound() {
        when(schoolClassService.getById(1L)).thenReturn(schoolClass);
        when(subjectService.getById(2L)).thenReturn(subject);
        when(classSubjectRepository.findBySchoolClassAndSubject(schoolClass, subject))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                classSubjectService.updateTeacher(1L, 2L, 4L));
    }

    @Test
    void getById_shouldReturnClassSubject() {
        when(classSubjectRepository.findById(10L)).thenReturn(Optional.of(classSubject));

        ClassSubject result = classSubjectService.getById(10L);

        assertEquals(classSubject, result);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(classSubjectRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> classSubjectService.getById(10L));
    }

    @Test
    void deleteById_shouldDelete() {
        classSubjectService.deleteById(10L);
        verify(classSubjectRepository).deleteById(10L);
    }
}