package com.github.yaroglek.edudiary.extern.controller.rest;

import com.github.yaroglek.edudiary.app.service.ScheduleDayService;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import com.github.yaroglek.edudiary.extern.assembler.ScheduleDayAssembler;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import com.github.yaroglek.edudiary.extern.dto.ScheduleDayDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule-days")
@RequiredArgsConstructor
public class ScheduleDayController {

    private final ScheduleDayService scheduleDayService;
    private final ScheduleDayAssembler scheduleDayAssembler;

    @PostMapping
    public ResponseEntity<ScheduleDayDto> createDay(@Valid @RequestBody ScheduleDayDto dto) {
        ScheduleDay saved = scheduleDayService.create(scheduleDayAssembler.toEntity(dto));
        return ResponseEntity.ok(scheduleDayAssembler.toModel(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDayDto> getDay(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleDayAssembler.toModel(scheduleDayService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteDay(@PathVariable Long id) {
        scheduleDayService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("Schedule day with ID " + id + " deleted"));
    }

    @GetMapping("/class/{classId}/week")
    public ResponseEntity<CollectionModel<ScheduleDayDto>> getScheduleDaysForWeek(
            @PathVariable Long classId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<ScheduleDay> days = scheduleDayService.getByClassAndWeek(classId, date);
        List<ScheduleDayDto> dtos = days.stream()
                .map(scheduleDayAssembler::toModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(dtos));
    }
}
