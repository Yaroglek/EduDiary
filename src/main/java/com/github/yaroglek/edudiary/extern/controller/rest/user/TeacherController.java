package com.github.yaroglek.edudiary.extern.controller.rest.user;

import com.github.yaroglek.edudiary.app.service.user.TeacherService;
import com.github.yaroglek.edudiary.domain.users.Teacher;
import com.github.yaroglek.edudiary.extern.assembler.user.TeacherAssembler;
import com.github.yaroglek.edudiary.extern.dto.user.TeacherDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/teachers")
public class TeacherController extends GenericUserController<Teacher, TeacherDto, TeacherService, TeacherAssembler> {

    public TeacherController(TeacherService service, TeacherAssembler assembler) {
        super(service, assembler);
    }

    @Override
    protected String getEntityName() {
        return "Teacher";
    }
}
