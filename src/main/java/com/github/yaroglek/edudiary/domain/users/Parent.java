package com.github.yaroglek.edudiary.domain.users;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"children"})
@ToString(callSuper = true, exclude = {"children"})
@Entity
@DiscriminatorValue("PARENT")
@NoArgsConstructor
public class Parent extends User {
    public Parent(String username, String email, String password, String fullName) {
        super(username, email, password, fullName);
    }

    @ManyToMany
    @JoinTable(
            name = "parent_student",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> children = new HashSet<>();

    @Override
    public Role getRole() {
        return Role.PARENT;
    }
}
