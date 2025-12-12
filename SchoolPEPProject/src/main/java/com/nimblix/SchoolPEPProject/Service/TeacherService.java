package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TeacherService {
    Map<String, String> registerTeacher(TeacherRegistrationRequest request);

    ResponseEntity<?> getTeacherDetails(Long teacherId);

    ResponseEntity<Map<String, String>> createClassroom(ClassroomRequest request);
}
