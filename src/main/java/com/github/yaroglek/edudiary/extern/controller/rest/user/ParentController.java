package com.github.yaroglek.edudiary.extern.controller.rest.user;

import com.github.yaroglek.edudiary.app.service.user.ParentService;
import com.github.yaroglek.edudiary.domain.users.Parent;
import com.github.yaroglek.edudiary.extern.assembler.user.ParentAssembler;
import com.github.yaroglek.edudiary.extern.dto.user.ParentDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/parents")
public class ParentController extends GenericUserController<Parent, ParentDto, ParentService, ParentAssembler> {

    public ParentController(ParentService service, ParentAssembler assembler) {
        super(service, assembler);
    }

    @Override
    protected Parent toEntity(ParentDto dto) {
        return assembler.toEntity(dto);
    }

    @Override
    protected ParentDto toModel(Parent entity) {
        return assembler.toModel(entity);
    }

    @Override
    protected String getEntityName() {
        return "Parent";
    }
}

