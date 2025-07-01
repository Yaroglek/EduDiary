package com.github.yaroglek.edudiary.domain.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
