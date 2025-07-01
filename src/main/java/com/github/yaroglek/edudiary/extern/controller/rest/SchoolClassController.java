package com.github.yaroglek.edudiary.extern.controller.rest;

import com.github.yaroglek.edudiary.app.service.SchoolClassService;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import com.github.yaroglek.edudiary.extern.assembler.SchoolClassAssembler;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import com.github.yaroglek.edudiary.extern.dto.SchoolClassDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class SchoolClassController {

    private final SchoolClassService schoolClassService;
    private final SchoolClassAssembler schoolClassAssembler;

    @PostMapping
    public ResponseEntity<SchoolClassDto> createClass(@Valid @RequestBody SchoolClassDto dto) {
        SchoolClass saved = schoolClassService.create(schoolClassAssembler.toEntity(dto));
        return ResponseEntity.ok(schoolClassAssembler.toModel(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolClassDto> getClass(@PathVariable Long id) {
        return ResponseEntity.ok(schoolClassAssembler.toModel(schoolClassService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteClass(@PathVariable Long id) {
        schoolClassService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("Class with ID " + id + " deleted"));
    }

    @PostMapping("/{classId}/students/{studentId}")
    public ResponseEntity<MessageDto> addStudentToClass(@PathVariable Long classId, @PathVariable Long studentId) {
        schoolClassService.addStudent(classId, studentId);
        return ResponseEntity.ok(new MessageDto("Student " + studentId + " added to class " + classId));
    }

    @DeleteMapping("/{classId}/students/{studentId}")
    public ResponseEntity<MessageDto> removeStudentFromClass(@PathVariable Long classId, @PathVariable Long studentId) {
        schoolClassService.removeStudent(classId, studentId);
        return ResponseEntity.ok(new MessageDto("Student " + studentId + " removed from class " + classId));
    }
}
