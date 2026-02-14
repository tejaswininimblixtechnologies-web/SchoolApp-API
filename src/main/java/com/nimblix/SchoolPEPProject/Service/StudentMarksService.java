package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.StudentMarks;
import com.nimblix.SchoolPEPProject.Request.StudentMarksRequest;

import java.util.List;

public interface StudentMarksService {

    List<StudentMarks> getMarksByStudentId(Long studentId);

    StudentMarks saveOrUpdateMarks(StudentMarksRequest request);
}