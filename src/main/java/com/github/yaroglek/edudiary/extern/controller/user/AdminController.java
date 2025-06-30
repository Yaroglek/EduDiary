package com.github.yaroglek.edudiary.extern.controller.user;

import com.github.yaroglek.edudiary.app.service.user.AdminService;
import com.github.yaroglek.edudiary.domain.users.Admin;
import com.github.yaroglek.edudiary.extern.assembler.user.AdminAssembler;
import com.github.yaroglek.edudiary.extern.dto.user.AdminDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        Admin admin = adminAssembler.toEntity(dto);
        Admin saved = adminService.create(admin);
        return new ResponseEntity<>(adminAssembler.toModel(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDto> get(@PathVariable Long id) {
        Admin found = adminService.getById(id);
        return ResponseEntity.ok(adminAssembler.toModel(found));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
