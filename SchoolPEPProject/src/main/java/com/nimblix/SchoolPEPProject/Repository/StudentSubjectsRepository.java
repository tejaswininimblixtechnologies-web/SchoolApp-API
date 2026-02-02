package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.StudentSubjects;
import com.nimblix.SchoolPEPProject.Model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSubjectsRepository extends JpaRepository<StudentSubjects, Long> {

    List<StudentSubjects> findByStudentId(Long studentId);

    List<StudentSubjects> findByStudentIdAndAcademicYear(Long studentId, String academicYear);

    @Query("SELECT ss.subject FROM StudentSubjects ss WHERE ss.student.id = :studentId AND ss.academicYear = :academicYear AND ss.status = :status")
    List<Subject> findSubjectsByStudentAndAcademicYear(@Param("studentId") Long studentId, @Param("academicYear") String academicYear, @Param("status") String status);
}
