package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import com.github.yaroglek.edudiary.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long> {
    boolean existsBySchoolClassAndSubject(SchoolClass schoolClass, Subject subject);

    Optional<ClassSubject> findBySchoolClassAndSubject(SchoolClass schoolClass, Subject subject);

    List<ClassSubject> findBySchoolClass(SchoolClass schoolClass);

}
