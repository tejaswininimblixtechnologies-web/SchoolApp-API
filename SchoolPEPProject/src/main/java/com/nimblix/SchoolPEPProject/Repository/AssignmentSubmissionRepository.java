package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    Optional<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    @Query("SELECT sub FROM AssignmentSubmission sub WHERE sub.student.id = :studentId " +
           "AND (:assignmentId IS NULL OR sub.assignment.id = :assignmentId) " +
           "ORDER BY sub.submissionDate DESC")
    List<AssignmentSubmission> findByStudentIdWithFilters(@Param("studentId") Long studentId,
                                                          @Param("assignmentId") Long assignmentId);

    @Query("SELECT sub FROM AssignmentSubmission sub WHERE sub.student.id = :studentId AND sub.assignment.id = :assignmentId")
    Optional<AssignmentSubmission> findByStudentAndAssignment(@Param("studentId") Long studentId,
                                                            @Param("assignmentId") Long assignmentId);
}
