package com.github.yaroglek.edudiary.extern.controller.rest;

import com.github.yaroglek.edudiary.app.service.ScheduleService;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.extern.assembler.LessonAssembler;
import com.github.yaroglek.edudiary.extern.dto.LessonDto;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.util.List;

@Controller
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final LessonAssembler lessonAssembler;

    @PostMapping("/generate")
    public ResponseEntity<MessageDto> generateWeeklyLessons(@RequestParam Long classId,
                                                            @RequestParam DayOfWeek dayOfWeek,
                                                            @Valid @RequestBody List<LessonDto> lessonDtos) {
        List<Lesson> templates = lessonDtos.stream()
                .map(lessonAssembler::toEntity)
                .toList();

        scheduleService.generateWeeklyLessonsUntilYearEnd(classId, dayOfWeek, templates);
        return ResponseEntity.ok(new MessageDto("Schedule generated for every week on " + dayOfWeek));
    }
}

