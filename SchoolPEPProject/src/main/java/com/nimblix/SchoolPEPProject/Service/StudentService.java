package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.StudentRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.UpdateStudentProfileRequest;
import com.nimblix.SchoolPEPProject.Response.StudentDetailsResponse;
import com.nimblix.SchoolPEPProject.Response.StudentContextResponse;
import com.nimblix.SchoolPEPProject.Response.WeeklyTimetableResponse;
import com.nimblix.SchoolPEPProject.Response.TimetableNoteResponse;
import com.nimblix.SchoolPEPProject.Response.AssignmentFilterResponse;
import com.nimblix.SchoolPEPProject.Response.AssignmentSearchResponse;
import com.nimblix.SchoolPEPProject.Response.AssignmentDetailsResponse;
import com.nimblix.SchoolPEPProject.Response.AssignmentSubmissionResponse;
import com.nimblix.SchoolPEPProject.Response.StudentProfileResponse;
import com.nimblix.SchoolPEPProject.Response.DashboardSummaryResponse;
import com.nimblix.SchoolPEPProject.Response.SubjectResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface StudentService {
    ResponseEntity<?> registerStudent(StudentRegistrationRequest studentRegistrationRequest);


    void deleteStudent(Long studentId);

    void updateStudentDetails(Long studentId, StudentRegistrationRequest request);

    List<StudentDetailsResponse> getStudentsBySchoolId(Long schoolId);

    StudentContextResponse getStudentContext(String email);

    List<WeeklyTimetableResponse> getWeeklyTimetable(String email);

    List<TimetableNoteResponse> getTimetableNotes(String email, String subject, String dayOfWeek, Integer periodNumber);

    // Assignment Management APIs
    AssignmentFilterResponse getAssignmentFilters(String email);

    List<AssignmentSearchResponse> searchAssignments(String email, String subject, String status);

    AssignmentDetailsResponse getAssignmentDetails(String email, Long assignmentId);

    ResponseEntity<?> uploadAssignment(String email, Long assignmentId, MultipartFile file, String remarks);

    AssignmentSubmissionResponse getAssignmentSubmissionStatus(String email, Long assignmentId);

    // Student Profile APIs
    StudentProfileResponse getStudentProfile(String email);

    ResponseEntity<?> updateStudentProfile(String email, UpdateStudentProfileRequest request);

    // Dashboard Summary API
    DashboardSummaryResponse getDashboardSummary(String email);

    // Supporting APIs
    List<SubjectResponse> getStudentSubjects(String email);

//    void updateStudentDetails(Integer studentId, StudentRegistrationRequest request);
}
