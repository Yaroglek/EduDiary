package com.github.yaroglek.edudiary.extern.controller.template;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.app.service.ClassSubjectService;
import com.github.yaroglek.edudiary.app.service.SchoolClassService;
import com.github.yaroglek.edudiary.app.service.SubjectService;
import com.github.yaroglek.edudiary.app.service.user.AdminService;
import com.github.yaroglek.edudiary.app.service.user.ParentService;
import com.github.yaroglek.edudiary.app.service.user.StudentService;
import com.github.yaroglek.edudiary.app.service.user.TeacherService;
import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.domain.users.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final SchoolClassService schoolClassService;
    private final SubjectService subjectService;
    private final ClassSubjectService classSubjectService;

    private final AdminService adminService;
    private final StudentService studentService;
    private final ParentService parentService;
    private final TeacherService teacherService;

    private final UserRepository userRepository;

    @GetMapping("/users")
    public String usersPage() {
        return "admin/users";
    }

    @GetMapping("/users/list")
    @ResponseBody
    public List<UserDto> listUsersByRole(@RequestParam("role") String roleStr) {
        Role role = Role.valueOf(roleStr);

        return switch (role) {
            case ADMIN -> adminService.getAll().stream().map(UserDto::from).toList();
            case STUDENT -> studentService.getAll().stream().map(UserDto::from).toList();
            case PARENT -> parentService.getAll().stream().map(UserDto::from).toList();
            case TEACHER -> teacherService.getAll().stream().map(UserDto::from).toList();
        };
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam String username,
                          @RequestParam String fullName,
                          @RequestParam String password,
                          @RequestParam String email,
                          @RequestParam String role,
                          Model model) {

        Role userRole = Role.valueOf(role);

        switch (userRole) {
            case ADMIN -> adminService.create(new Admin(username, email, password, fullName));
            case STUDENT -> studentService.create(new Student(username, email, password, fullName));
            case PARENT -> parentService.create(new Parent(username, email, password, fullName));
            case TEACHER -> teacherService.create(new Teacher(username, email, password, fullName));
            default -> throw new IllegalArgumentException("Неизвестная роль: " + role);
        }

        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/delete/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            switch (user.getRole()) {
                case ADMIN -> adminService.deleteById(user.getId());
                case STUDENT -> studentService.deleteById(user.getId());
                case PARENT -> parentService.deleteById(user.getId());
                case TEACHER -> teacherService.deleteById(user.getId());
            }
        });
    }

    @GetMapping("/classes")
    public String listClasses(Model model) {
        List<SchoolClass> classes = schoolClassService.getAll();
        model.addAttribute("classes", classes);
        return "admin/classes";
    }

    @PostMapping("/classes/create")
    public String createClass(@RequestParam("grade") Integer grade,
                              @RequestParam("literal") String literal,
                              RedirectAttributes redirectAttributes) {

        SchoolClass schoolClass = SchoolClass.builder()
                .grade(grade)
                .literal(literal)
                .build();


        try {
            schoolClassService.create(schoolClass);
            redirectAttributes.addFlashAttribute("successMessage", "Класс успешно создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании класса");
        }

        return "redirect:/admin/classes";
    }

    @GetMapping("/classes/{classId}")
    public String classDetails(@PathVariable Long classId, Model model) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        Set<Student> students = schoolClass.getStudents();
        List<Parent> parents = parentService.getAll();

        model.addAttribute("schoolClass", schoolClass);
        model.addAttribute("students", students);
        model.addAttribute("parents", parents);

        return "admin/class-details";
    }


    @GetMapping("/classes/{classId}/add-student")
    public String addStudentToClassPage(@PathVariable Long classId, Model model) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        List<Student> studentsWithoutClass = studentService.findStudentsWithoutClass();
        model.addAttribute("schoolClass", schoolClass);
        model.addAttribute("studentsWithoutClass", studentsWithoutClass);
        return "admin/class-add-student";
    }

    @PostMapping("/classes/{classId}/add-student/{studentId}")
    public String addStudentToClass(@PathVariable Long classId, @PathVariable Long studentId) {
        schoolClassService.addStudent(classId, studentId);
        return "redirect:/admin/classes/" + classId;
    }

    @PostMapping("/classes/{classId}/remove-student/{studentId}")
    public String removeStudentFromClass(@PathVariable Long classId, @PathVariable Long studentId) {
        schoolClassService.removeStudent(classId, studentId);
        return "redirect:/admin/classes/" + classId;
    }

    @GetMapping("/subjects")
    public String listSubjects(Model model) {
        List<Subject> subjects = subjectService.getAll();
        model.addAttribute("subjects", subjects);
        return "admin/subjects";
    }

    @PostMapping("/subjects/create")
    public String createSubject(@RequestParam String name) {
        subjectService.create(Subject.builder().name(name).build());
        return "redirect:/admin/subjects";
    }

    @GetMapping("/subjects/{subjectId}")
    public String subjectDetail(@PathVariable Long subjectId, Model model) {
        Subject subject = subjectService.getById(subjectId);
        Set<Teacher> assignedTeachers = subject.getTeachers();
        List<Teacher> availableTeachers = teacherService.findTeachersNotAssignedToSubject(subject);
        model.addAttribute("subject", subject);
        model.addAttribute("assignedTeachers", assignedTeachers);
        model.addAttribute("availableTeachers", availableTeachers);
        return "admin/subject-details";
    }

    @PostMapping("/subjects/{subjectId}/add-teacher")
    public String addTeacherToSubject(@PathVariable Long subjectId,
                                      @RequestParam Long teacherId) {
        subjectService.assignTeacher(subjectId, teacherId);
        return "redirect:/admin/subjects/" + subjectId;
    }

    @GetMapping("/subjects/{subjectId}/remove-teacher/{teacherId}")
    public String removeTeacherFromSubject(@PathVariable Long subjectId,
                                           @PathVariable Long teacherId) {
        subjectService.removeTeacher(subjectId, teacherId);
        return "redirect:/admin/subjects/" + subjectId;
    }

    @DeleteMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectService.deleteById(id);
        return "redirect:/admin/subjects";
    }

    @GetMapping("/classes/{classId}/subjects")
    public String getClassSubjects(@PathVariable Long classId, Model model) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        List<ClassSubject> classSubjects = classSubjectService.findBySchoolClass(schoolClass);
        List<Subject> allSubjects = subjectService.getAll();

        model.addAttribute("schoolClass", schoolClass);
        model.addAttribute("classSubjects", classSubjects);
        model.addAttribute("allSubjects", allSubjects);

        return "admin/class-subjects";
    }

    @GetMapping("/classes/subjects/{subjectId}/teachers")
    @ResponseBody
    public List<TeacherShortDto> getTeachersBySubject(@PathVariable Long subjectId) {
        Subject subject = subjectService.getById(subjectId);
        return subject.getTeachers().stream()
                .map(t -> new TeacherShortDto(t.getId(), t.getFullName()))
                .toList();
    }

    @PostMapping("/classes/{classId}/subjects/create")
    public String createClassSubject(@PathVariable Long classId,
                                     @RequestParam Long subjectId,
                                     @RequestParam Long teacherId) {
        classSubjectService.assignSubjectToClass(classId, subjectId, teacherId);
        return "redirect:/admin/classes/" + classId + "/subjects";
    }

    @PostMapping("/classes/{classId}/subjects/{classSubjectId}/delete")
    public String deleteClassSubject(@PathVariable Long classId, @PathVariable Long classSubjectId) {
        classSubjectService.deleteById(classSubjectId);
        return "redirect:/admin/classes/" + classId + "/subjects";
    }

    @GetMapping("/schedule")
    public String scheduleTemplatePage(Model model) {
        List<SchoolClass> allClasses = schoolClassService.getAll();
        model.addAttribute("allClasses", allClasses);

        return "admin/schedule";
    }

    @GetMapping("/classes/{classId}/subjects-list")
    @ResponseBody
    public List<ClassSubjectDto> getClassSubjects(@PathVariable Long classId) {
        SchoolClass schoolClass = schoolClassService.getById(classId);
        List<ClassSubject> classSubjects = classSubjectService.findBySchoolClass(schoolClass);

        return classSubjects.stream()
                .map(cs -> new ClassSubjectDto(
                        cs.getId(),
                        cs.getSubject().getName(),
                        cs.getTeacher().getFullName()
                ))
                .toList();
    }

    @PostMapping("/parents/assign")
    public String assignParentToStudent(@RequestParam Long parentId,
                                        @RequestParam Long studentId,
                                        @RequestParam Long classId,
                                        RedirectAttributes redirectAttributes) {
        try {
            parentService.addChild(parentId, studentId);
            redirectAttributes.addFlashAttribute("successMessage", "Родитель успешно назначен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при назначении родителя: " + e.getMessage());
        }
        return "redirect:/admin/classes/" + classId;
    }


    public record UserDto(Long id, String username, String fullName, String email) {
        static UserDto from(User user) {
            return new UserDto(user.getId(), user.getUsername(), user.getFullName(), user.getEmail());
        }
    }

    public record TeacherShortDto(Long id, String fullName) {}

    public record ClassSubjectDto(Long id, String subjectName, String teacherName) {}

}

