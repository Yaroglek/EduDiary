package com.github.yaroglek.edudiary.extern.controller.rest.user;

import com.github.yaroglek.edudiary.app.service.user.StudentService;
import com.github.yaroglek.edudiary.domain.users.Student;
import com.github.yaroglek.edudiary.extern.assembler.user.StudentAssembler;
import com.github.yaroglek.edudiary.extern.dto.user.StudentDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/students")
public class StudentController extends GenericUserController<Student, StudentDto, StudentService, StudentAssembler> {

    public StudentController(StudentService service, StudentAssembler assembler) {
        super(service, assembler);
    }

    @Override
    protected String getEntityName() {
        return "Student";
    }
}
