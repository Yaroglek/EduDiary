package com.github.yaroglek.edudiary.domain.users;

import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"subjects", "classSubjects"})
@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User {

    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<ClassSubject> classSubjects = new HashSet<>();

    @Override
    public Role getRole() {
        return Role.TEACHER;
    }
}
