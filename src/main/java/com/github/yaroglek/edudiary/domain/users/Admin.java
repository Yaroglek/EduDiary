package com.github.yaroglek.edudiary.domain.users;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@NoArgsConstructor
public class Admin extends User {
    public Admin(String username, String email, String password, String fullName) {
        super(username, email, password, fullName);
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
