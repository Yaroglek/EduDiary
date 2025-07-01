package com.github.yaroglek.edudiary.extern.controller.rest;

import com.github.yaroglek.edudiary.app.service.MarkService;
import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.extern.assembler.MarkAssembler;
import com.github.yaroglek.edudiary.extern.dto.MarkDto;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
public class MarkController {

    private final MarkService markService;
    private final MarkAssembler markAssembler;

    @PostMapping
    public ResponseEntity<MarkDto> createMark(@Valid @RequestBody MarkDto markDto) {
        Mark mark = markAssembler.toEntity(markDto);
        Mark saved = markService.create(mark);
        return ResponseEntity.ok(markAssembler.toModel(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkDto> getMark(@PathVariable Long id) {
        Mark mark = markService.getById(id);
        return ResponseEntity.ok(markAssembler.toModel(mark));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteMark(@PathVariable Long id) {
        markService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("Mark with ID " + id + " deleted"));
    }
}
