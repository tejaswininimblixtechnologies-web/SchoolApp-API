package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.ClassesConductedRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.ClassesConductedResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherProfileResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherAssignedClassesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TeacherService {
    Map<String, String> registerTeacher(TeacherRegistrationRequest request);

//    ResponseEntity<Teacher> getTeacherDetails(Long teacherId);

    ResponseEntity<Map<String, String>> createClassroom(ClassroomRequest request);

    public TeacherDetailsResponse getTeacherDetails(Long teacherId);

    Map<String, String> updateTeacherDetails(TeacherRegistrationRequest request, Long teacherId);

    Map<String, String> deleteTeacherDetails(Long teacherId, Long schoolId);

    Map<String, String> createAssignment(CreateAssignmentRequest request, MultipartFile[] files);

    Map<String, String> onboardSubject(OnboardSubjectRequest request);

    Map<String, String> updateAssignment(CreateAssignmentRequest request, MultipartFile[] files);

    Map<String, String> updateOnboardSubject(OnboardSubjectRequest request);

    Map<String, String> deleteAssignment(Long assignmentId, Long subjectId);

    TeacherProfileResponse getLoggedInTeacherDetails();

    TeacherAssignedClassesResponse getTeacherAssignedClassesAndSubjects();

    // ========== CLASSES CONDUCTED METHODS ==========
    
    // CRUD Operations
    ResponseEntity<ClassesConductedResponse> createClass(ClassesConductedRequest request);
    ResponseEntity<ClassesConductedResponse> updateClass(Long classId, ClassesConductedRequest request);
    ResponseEntity<String> deleteClass(Long classId);
    ResponseEntity<ClassesConductedResponse> getClassById(Long classId);

    // Teacher Classes Operations
    ResponseEntity<List<ClassesConductedResponse>> getTeacherClasses();
    ResponseEntity<List<ClassesConductedResponse>> getClassesByDate(LocalDate classDate);
    ResponseEntity<List<ClassesConductedResponse>> getClassesByDateRange(LocalDate startDate, LocalDate endDate);
    ResponseEntity<List<ClassesConductedResponse>> getTodayClasses();

}
