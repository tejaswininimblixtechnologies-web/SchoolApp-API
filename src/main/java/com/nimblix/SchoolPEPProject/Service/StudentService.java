package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.StudentRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.StudentDetailsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    ResponseEntity<?> registerStudent(StudentRegistrationRequest studentRegistrationRequest);


    void deleteStudent(Long studentId);

    void updateStudentDetails(Long studentId, StudentRegistrationRequest request);

    List<StudentDetailsResponse> getStudentsBySchoolId(Long schoolId);

//    void updateStudentDetails(Integer studentId, StudentRegistrationRequest request);

    List<StudentDetailsResponse> getAllStudents();
}
