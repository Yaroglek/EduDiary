package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
