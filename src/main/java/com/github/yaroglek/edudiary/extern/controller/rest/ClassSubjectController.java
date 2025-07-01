package com.github.yaroglek.edudiary.extern.controller.rest;

import com.github.yaroglek.edudiary.app.service.ClassSubjectService;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/class-subjects")
@RequiredArgsConstructor
public class ClassSubjectController {

    private final ClassSubjectService classSubjectService;

    @PostMapping("/assign")
    public ResponseEntity<MessageDto> assignSubjectToClass(@RequestParam Long classId,
                                                           @RequestParam Long subjectId,
                                                           @RequestParam Long teacherId) {
        classSubjectService.assignSubjectToClass(classId, subjectId, teacherId);
        return ResponseEntity.ok(new MessageDto("Created"));
    }

    @DeleteMapping("/unassign")
    public ResponseEntity<MessageDto> removeSubjectFromClass(@RequestParam Long classId,
                                                             @RequestParam Long subjectId) {
        classSubjectService.removeSubjectFromClass(classId, subjectId);
        return ResponseEntity.ok(new MessageDto("Subject " + subjectId + " unassigned from class " + classId));
    }

    @PutMapping("/update-teacher")
    public ResponseEntity<MessageDto> updateTeacher(@RequestParam Long classId,
                                                    @RequestParam Long subjectId,
                                                    @RequestParam Long newTeacherId) {
        classSubjectService.updateTeacher(classId, subjectId, newTeacherId);
        return ResponseEntity.ok(new MessageDto("Teacher updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteById(@PathVariable Long id) {
        classSubjectService.deleteById(id);
        return ResponseEntity.ok(new MessageDto("ClassSubject with ID " + id + " has been deleted"));
    }
}
