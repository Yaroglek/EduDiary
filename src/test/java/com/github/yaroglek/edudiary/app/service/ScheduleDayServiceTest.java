package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ScheduleDayRepository;
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
class ScheduleDayServiceTest {

    @Mock
    private ScheduleDayRepository scheduleDayRepository;

    @InjectMocks
    private ScheduleDayService scheduleDayService;

    private ScheduleDay scheduleDay;

    @BeforeEach
    void setUp() {
        scheduleDay = new ScheduleDay();
        scheduleDay.setId(1L);
        scheduleDay.setDate(LocalDate.of(2024, 9, 1));
    }

    @Test
    void create_shouldSaveScheduleDay() {
        when(scheduleDayRepository.save(scheduleDay)).thenReturn(scheduleDay);

        ScheduleDay saved = scheduleDayService.create(scheduleDay);

        assertNotNull(saved);
        assertEquals(LocalDate.of(2024, 9, 1), saved.getDate());
        verify(scheduleDayRepository).save(scheduleDay);
    }

    @Test
    void create_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () -> scheduleDayService.create(null));
        verify(scheduleDayRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnScheduleDay() {
        when(scheduleDayRepository.findById(1L)).thenReturn(Optional.of(scheduleDay));

        ScheduleDay found = scheduleDayService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(scheduleDayRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(scheduleDayRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> scheduleDayService.getById(1L));
        verify(scheduleDayRepository).findById(1L);
    }

    @Test
    void deleteById_shouldCallRepository() {
        scheduleDayService.deleteById(1L);
        verify(scheduleDayRepository).deleteById(1L);
    }
}
