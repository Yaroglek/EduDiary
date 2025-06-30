package com.github.yaroglek.edudiary.extern.controller;

import com.github.yaroglek.edudiary.app.service.SubjectService;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.extern.assembler.SubjectAssembler;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import com.github.yaroglek.edudiary.extern.dto.SubjectDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;
    private final SubjectAssembler subjectAssembler;

    @PostMapping
    public ResponseEntity<SubjectDto> createSubject(@Valid @RequestBody SubjectDto dto) {
        Subject saved = subjectService.create(subjectAssembler.toEntity(dto));
        return new ResponseEntity<>(subjectAssembler.toModel(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getSubject(@PathVariable Long id) {
        return ResponseEntity.ok(subjectAssembler.toModel(subjectService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteSubject(@PathVariable Long id) {
        subjectService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("Subject with ID " + id + " deleted"));
    }
}
