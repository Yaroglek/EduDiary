package com.github.yaroglek.edudiary.extern.assembler.user;

import com.github.yaroglek.edudiary.domain.users.Admin;
import com.github.yaroglek.edudiary.extern.controller.user.AdminController;
import com.github.yaroglek.edudiary.extern.dto.user.AdminDto;
import org.springframework.stereotype.Component;

@Component
public class AdminAssembler extends UserAssembler<Admin, AdminDto> {

    public AdminAssembler() {
        super(AdminController.class, AdminDto.class);
    }

    @Override
    public AdminDto toModel(Admin entity) {
        return super.toModel(entity);
    }

    public Admin toEntity(AdminDto dto) {
        Admin admin = new Admin();
        super.toEntity(dto, admin);
        return admin;
    }
}

