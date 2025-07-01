package com.github.yaroglek.edudiary.extern.controller.rest.user;

import com.github.yaroglek.edudiary.app.service.user.AdminService;
import com.github.yaroglek.edudiary.domain.users.Admin;
import com.github.yaroglek.edudiary.extern.assembler.user.AdminAssembler;
import com.github.yaroglek.edudiary.extern.dto.user.AdminDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/admins")
public class AdminController extends GenericUserController<Admin, AdminDto, AdminService, AdminAssembler> {

    public AdminController(AdminService service, AdminAssembler assembler) {
        super(service, assembler);
    }

    @Override
    protected Admin toEntity(AdminDto dto) {
        return assembler.toEntity(dto);
    }

    @Override
    protected AdminDto toModel(Admin entity) {
        return assembler.toModel(entity);
    }

    @Override
    protected String getEntityName() {
        return "Admin";
    }
}

