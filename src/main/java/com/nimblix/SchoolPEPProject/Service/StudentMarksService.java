package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.StudentMarks;

import java.util.List;

public interface StudentMarksService {

    List<StudentMarks> getMarksByStudentId(Long studentId);
}