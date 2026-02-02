package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT DISTINCT a.subject FROM Assignment a WHERE a.classId = :classId AND a.section = :section AND a.academicYear = :academicYear")
    List<String> findDistinctSubjectsByClassSectionAndAcademicYear(@Param("classId") Long classId, 
                                                                  @Param("section") String section, 
                                                                  @Param("academicYear") String academicYear);

    @Query("SELECT a FROM Assignment a WHERE a.classId = :classId AND a.section = :section AND a.academicYear = :academicYear " +
           "AND (:subject IS NULL OR a.subject = :subject) " +
           "ORDER BY a.dueDate DESC")
    List<Assignment> findByClassSectionAcademicYearWithFilters(@Param("classId") Long classId, 
                                                               @Param("section") String section, 
                                                               @Param("academicYear") String academicYear,
                                                               @Param("subject") String subject);

    @Query("SELECT a FROM Assignment a WHERE a.classId = :classId AND a.section = :section AND a.academicYear = :academicYear " +
           "AND (:subject IS NULL OR a.subject = :subject) " +
           "AND (:status IS NULL OR " +
           "CASE " +
           "WHEN :status = 'PENDING' THEN NOT EXISTS (SELECT 1 FROM AssignmentSubmission sub WHERE sub.assignment.id = a.id AND sub.student.id = :studentId) " +
           "WHEN :status = 'SUBMITTED' THEN EXISTS (SELECT 1 FROM AssignmentSubmission sub WHERE sub.assignment.id = a.id AND sub.student.id = :studentId) " +
           "WHEN :status = 'LATE' THEN EXISTS (SELECT 1 FROM AssignmentSubmission sub WHERE sub.assignment.id = a.id AND sub.student.id = :studentId AND sub.submissionStatus = 'LATE') " +
           "WHEN :status = 'EVALUATED' THEN EXISTS (SELECT 1 FROM AssignmentSubmission sub WHERE sub.assignment.id = a.id AND sub.student.id = :studentId AND sub.evaluationStatus = 'EVALUATED') " +
           "END) " +
           "ORDER BY a.dueDate DESC")
    List<Assignment> findByClassSectionAcademicYearWithStatusFilter(@Param("classId") Long classId, 
                                                                   @Param("section") String section, 
                                                                   @Param("academicYear") String academicYear,
                                                                   @Param("subject") String subject,
                                                                   @Param("status") String status,
                                                                   @Param("studentId") Long studentId);
}
