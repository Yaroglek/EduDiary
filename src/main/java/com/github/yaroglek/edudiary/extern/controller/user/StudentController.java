package com.github.yaroglek.edudiary.extern.controller.user;

import com.github.yaroglek.edudiary.app.service.user.StudentService;
import com.github.yaroglek.edudiary.domain.users.Student;
import com.github.yaroglek.edudiary.extern.assembler.user.StudentAssembler;
import com.github.yaroglek.edudiary.extern.dto.user.StudentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentAssembler studentAssembler;

    @PostMapping
    public ResponseEntity<StudentDto> create(@Valid @RequestBody StudentDto dto) {
        Student student = studentAssembler.toEntity(dto);
        Student saved = studentService.create(student);
        return new ResponseEntity<>(studentAssembler.toModel(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> get(@PathVariable Long id) {
        Student found = studentService.getById(id);
        return ResponseEntity.ok(studentAssembler.toModel(found));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
