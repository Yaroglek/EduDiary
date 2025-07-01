package com.github.yaroglek.edudiary.extern.controller.user;

import com.github.yaroglek.edudiary.app.service.user.AdminService;
import com.github.yaroglek.edudiary.domain.users.Admin;
import com.github.yaroglek.edudiary.extern.assembler.user.AdminAssembler;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import com.github.yaroglek.edudiary.extern.dto.user.AdminDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminAssembler adminAssembler;

    @PostMapping
    public ResponseEntity<AdminDto> create(@Valid @RequestBody AdminDto dto) {
        Admin saved = adminService.create(adminAssembler.toEntity(dto));
        return ResponseEntity.ok(adminAssembler.toModel(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDto> get(@PathVariable Long id) {
        Admin found = adminService.getById(id);
        return ResponseEntity.ok(adminAssembler.toModel(found));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> delete(@PathVariable Long id) {
        adminService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("Admin with ID " + id + " deleted"));
    }
}
