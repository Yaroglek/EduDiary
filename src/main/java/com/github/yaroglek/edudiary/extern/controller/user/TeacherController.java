package com.github.yaroglek.edudiary.extern.controller.user;

import com.github.yaroglek.edudiary.app.service.user.TeacherService;
import com.github.yaroglek.edudiary.domain.users.Teacher;
import com.github.yaroglek.edudiary.extern.assembler.user.TeacherAssembler;
import com.github.yaroglek.edudiary.extern.dto.user.TeacherDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherAssembler teacherAssembler;

    @PostMapping
    public ResponseEntity<TeacherDto> create(@Valid @RequestBody TeacherDto dto) {
        Teacher teacher = teacherAssembler.toEntity(dto);
        Teacher saved = teacherService.create(teacher);
        return new ResponseEntity<>(teacherAssembler.toModel(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> get(@PathVariable Long id) {
        Teacher found = teacherService.getById(id);
        return ResponseEntity.ok(teacherAssembler.toModel(found));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
