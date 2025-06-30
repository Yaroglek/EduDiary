package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByGradeAndLiteral(int name, String literal);
}
