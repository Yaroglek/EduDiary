package com.github.yaroglek.edudiary.extern.controller;

import com.github.yaroglek.edudiary.app.service.LessonService;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.extern.assembler.LessonAssembler;
import com.github.yaroglek.edudiary.extern.dto.LessonDto;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonAssembler lessonAssembler;

    @PostMapping
    public ResponseEntity<LessonDto> createLesson(@Valid @RequestBody LessonDto lessonDto) {
        Lesson lesson = lessonAssembler.toEntity(lessonDto);
        Lesson saved = lessonService.create(lesson);
        return ResponseEntity.ok(lessonAssembler.toModel(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLesson(@PathVariable Long id) {
        Lesson lesson = lessonService.getById(id);
        return ResponseEntity.ok(lessonAssembler.toModel(lesson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteLesson(@PathVariable Long id) {
        lessonService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("Lesson with ID " + id + " deleted"));
    }
}
