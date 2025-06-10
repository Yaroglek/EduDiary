package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long userId);

    List<Student> findBySchoolClassId(Long classId);
}
