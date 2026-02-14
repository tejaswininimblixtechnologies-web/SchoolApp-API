package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.StudentMarks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {

    List<StudentMarks> findByStudentId(Long studentId);
}