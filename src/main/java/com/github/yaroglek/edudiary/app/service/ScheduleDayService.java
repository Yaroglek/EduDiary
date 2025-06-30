package com.github.yaroglek.edudiary.app.service;

import com.github.yaroglek.edudiary.app.repository.ScheduleDayRepository;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ScheduleDayService {

    private final ScheduleDayRepository scheduleDayRepository;

    public ScheduleDay create(ScheduleDay scheduleDay) {
        if (scheduleDay == null) {
            throw new IllegalArgumentException("ScheduleDay is null");
        }

        ScheduleDay saved = scheduleDayRepository.save(scheduleDay);
        log.info("Создан день расписания: {}", saved.getDate());
        return saved;
    }

    public ScheduleDay getById(Long id) {
        ScheduleDay day = scheduleDayRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("День расписания с id={} не найден", id);
                    return new NoSuchElementException("ScheduleDay not found");
                });

        log.info("Получен день расписания: {}", day.getDate());
        return day;
    }

    public void deleteById(Long id) {
        log.info("Удаление дня расписания с id={}", id);
        scheduleDayRepository.deleteById(id);
    }
}

