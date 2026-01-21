package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRequest;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TeacherService {

    /* ========= TASK 1 ========= */
    Map<String, String> registerTeacher(TeacherRequest request);

    /* ========= TASK 2,3,4 ========= */
    List<TeacherResponse> getTeachers(
            Long schoolId,
            String search,
            Long subjectId
    );

    /* ========= TASK 5 ========= */
    TeacherDetailsResponse getTeacherDetails(Long teacherId);

    /* ========= TASK 6 ========= */
    Map<String, String> updateTeacherDetails(
            TeacherRequest request,
            Long teacherId
    );

    /* ========= TASK 7 ========= */
    Map<String, String> deleteTeacherDetails(
            Long teacherId,
            Long schoolId
    );

    /* ========= TASK 8 ========= */
    List<Map<String, Object>> getSubjectsBySchool(Long schoolId);

    /* ========= EXISTING CLASSROOM & ASSIGNMENT APIs ========= */

    ResponseEntity<Map<String, String>> createClassroom(
            ClassroomRequest request
    );

    Map<String, String> createAssignment(
            CreateAssignmentRequest request,
            MultipartFile[] files
    );

    Map<String, String> updateAssignment(
            CreateAssignmentRequest request,
            MultipartFile[] files
    );

    Map<String, String> deleteAssignment(
            Long assignmentId,
            Long subjectId
    );

    Map<String, String> onboardSubject(
            OnboardSubjectRequest request
    );

    Map<String, String> updateOnboardSubject(
            OnboardSubjectRequest request
    );
}
