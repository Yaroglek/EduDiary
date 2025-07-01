package com.github.yaroglek.edudiary.extern.controller.user;

import com.github.yaroglek.edudiary.app.service.user.ParentService;
import com.github.yaroglek.edudiary.domain.users.Parent;
import com.github.yaroglek.edudiary.extern.assembler.user.ParentAssembler;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import com.github.yaroglek.edudiary.extern.dto.user.ParentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;
    private final ParentAssembler parentAssembler;

    @PostMapping
    public ResponseEntity<ParentDto> create(@Valid @RequestBody ParentDto dto) {
        Parent saved = parentService.create(parentAssembler.toEntity(dto));
        return ResponseEntity.ok(parentAssembler.toModel(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentDto> get(@PathVariable Long id) {
        Parent found = parentService.getById(id);
        return ResponseEntity.ok(parentAssembler.toModel(found));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> delete(@PathVariable Long id) {
        parentService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("Parent with ID " + id + " deleted"));
    }
}
