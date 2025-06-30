package com.github.yaroglek.edudiary.domain.users;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"children"})
@Entity
@DiscriminatorValue("PARENT")
public class Parent extends User {

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
