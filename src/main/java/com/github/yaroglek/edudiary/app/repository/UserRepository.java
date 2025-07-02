package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.users.Student;
import com.github.yaroglek.edudiary.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("select s from Student s where s.schoolClass is null")
    List<Student> findStudentsWithoutClass();
}
