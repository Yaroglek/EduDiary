package com.github.yaroglek.edudiary.extern.controller.rest.user;

import com.github.yaroglek.edudiary.app.service.user.ParentService;
import com.github.yaroglek.edudiary.domain.users.Parent;
import com.github.yaroglek.edudiary.extern.assembler.user.ParentAssembler;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import com.github.yaroglek.edudiary.extern.dto.user.ParentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/parents")
public class ParentController extends GenericUserController<Parent, ParentDto, ParentService, ParentAssembler> {

    public ParentController(ParentService service, ParentAssembler assembler) {
        super(service, assembler);
    }

    @PostMapping("/{parentId}/children/{studentId}")
    public ResponseEntity<MessageDto> addChildToParent(@PathVariable Long parentId, @PathVariable Long studentId) {
        service.addChild(parentId, studentId);
        return ResponseEntity.ok(new MessageDto("Student " + studentId + " added to parent " + parentId));
    }

    @DeleteMapping("/{parentId}/children/{studentId}")
    public ResponseEntity<MessageDto> removeChildFromParent(@PathVariable Long parentId, @PathVariable Long studentId) {
        service.removeChild(parentId, studentId);
        return ResponseEntity.ok(new MessageDto("Student " + studentId + " removed from parent " + parentId));
    }

    @Override
    protected String getEntityName() {
        return "Parent";
    }
}

