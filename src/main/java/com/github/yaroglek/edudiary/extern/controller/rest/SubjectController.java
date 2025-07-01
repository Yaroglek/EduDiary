package com.github.yaroglek.edudiary.extern.controller.rest;

import com.github.yaroglek.edudiary.app.service.SubjectService;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.extern.assembler.SubjectAssembler;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import com.github.yaroglek.edudiary.extern.dto.SubjectDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(subjectAssembler.toModel(saved));
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

    @PostMapping("/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<MessageDto> assignTeacherToSubject(@PathVariable Long subjectId, @PathVariable Long teacherId) {
        subjectService.assignTeacher(subjectId, teacherId);
        return ResponseEntity.ok(new MessageDto("Teacher with ID " + teacherId + " assigned to subject " + subjectId));
    }

    @DeleteMapping("/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<MessageDto> removeTeacherFromSubject(@PathVariable Long subjectId, @PathVariable Long teacherId) {
        subjectService.removeTeacher(subjectId, teacherId);
        return ResponseEntity.ok(new MessageDto("Teacher with ID " + teacherId + " removed from subject " + subjectId));
    }

}
