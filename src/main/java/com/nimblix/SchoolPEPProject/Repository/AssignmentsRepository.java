package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Assignments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentsRepository extends JpaRepository<Assignments,Long> {

    Optional<Assignments> findByIdAndSubjectId(Long id, Long subjectId);

    long countByCreatedByUserId(Long teacherId);

}
