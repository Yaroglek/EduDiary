package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ScheduleDayRepository;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ScheduleDayService {

    private final ScheduleDayRepository scheduleDayRepository;

    /**
     * Метод для сохранения расписания на день
     *
     * @param scheduleDay - расписания для сохранения
     * @return - сохраненное расписание
     */
    public ScheduleDay create(ScheduleDay scheduleDay) {
        if (scheduleDay == null) {
            throw new IllegalArgumentException("ScheduleDay is null");
        }

        ScheduleDay saved = scheduleDayRepository.save(scheduleDay);
        log.info("ScheduleDay created for date {}", saved.getDate());
        return saved;
    }

    /**
     * Метод для получения расписания на день по ID
     *
     * @param id - ID расписания
     * @return - найденное расписание
     */
    public ScheduleDay getById(Long id) {
        ScheduleDay day = scheduleDayRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ScheduleDay with id {} not found", id);
                    return new NoSuchElementException("ScheduleDay not found");
                });

        log.info("ScheduleDay with id {} found for date {}", day.getId(), day.getDate());
        return day;
    }

    /**
     * Метод для удаления расписания на день по ID
     *
     * @param id - ID расписания
     */
    public void deleteById(Long id) {
        log.info("ScheduleDay with id {} deleted", id);
        scheduleDayRepository.deleteById(id);
    }

    /**
     * Метод для получения расписания на всю неделю для определенного класса
     *
     * @param classId   - ID класса
     * @param weekDate  - любая дата на требуемой неделе
     * @return - список всех расписаний на день для данной недели
     */
    public List<ScheduleDay> getByClassAndWeek(Long classId, LocalDate weekDate) {
        LocalDate monday = weekDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = weekDate.with(DayOfWeek.SUNDAY);

        List<ScheduleDay> days = scheduleDayRepository.findBySchoolClassIdAndDateBetween(classId, monday, sunday);

        log.info("Found {} schedule days for class {} from {} to {}", days.size(), classId, monday, sunday);

        return days;
    }
}

