package com.github.yaroglek.edudiary.domain.users;

import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"parents", "marks", "schoolClass"})
@ToString(callSuper = true, exclude = {"marks", "parents", "schoolClass"})
@Entity
@NoArgsConstructor
public class Student extends User {
    public Student(String username, String email, String password, String fullName) {
        super(username, email, password, fullName);
    }

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    @ManyToMany(mappedBy = "children")
    private Set<Parent> parents = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Mark> marks = new HashSet<>();

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }
}
