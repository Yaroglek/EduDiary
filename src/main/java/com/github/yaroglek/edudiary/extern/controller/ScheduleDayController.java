package com.github.yaroglek.edudiary.extern.controller;

import com.github.yaroglek.edudiary.app.service.ScheduleDayService;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import com.github.yaroglek.edudiary.extern.assembler.ScheduleDayAssembler;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import com.github.yaroglek.edudiary.extern.dto.ScheduleDayDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule-days")
@RequiredArgsConstructor
public class ScheduleDayController {
    private final ScheduleDayService scheduleDayService;
    private final ScheduleDayAssembler scheduleDayAssembler;

    @PostMapping
    public ResponseEntity<ScheduleDayDto> createDay(@Valid @RequestBody ScheduleDayDto dto) {
        ScheduleDay saved = scheduleDayService.create(scheduleDayAssembler.toEntity(dto));
        return new ResponseEntity<>(scheduleDayAssembler.toModel(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDayDto> getDay(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleDayAssembler.toModel(scheduleDayService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteDay(@PathVariable Long id) {
        scheduleDayService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("ScheduleDay with ID " + id + " deleted"));
    }
}
