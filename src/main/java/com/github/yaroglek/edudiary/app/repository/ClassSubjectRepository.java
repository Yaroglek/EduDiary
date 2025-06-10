package com.github.yaroglek.edudiary.app.repository;

import com.github.yaroglek.edudiary.domain.ClassSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long> {
    Optional<ClassSubject> findBySchoolClassIdAndSubjectId(Long classId, Long subjectId);

    List<ClassSubject> findByTeacherId(Long teacherId);

    List<ClassSubject> findBySchoolClassId(Long classId);
}
