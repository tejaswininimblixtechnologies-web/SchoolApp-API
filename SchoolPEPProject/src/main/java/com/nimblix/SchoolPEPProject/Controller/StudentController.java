package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.StudentRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.StudentDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    // REGISTER STUDENT
    @PostMapping("/register")
    public ResponseEntity<?> studentRegistration(
            @RequestBody StudentRegistrationRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            studentService.registerStudent(request);
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
            response.put(SchoolConstants.MESSAGE, "Student Registration Successful!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // GET STUDENTS BY SCHOOL
    @GetMapping("/details")
    public ResponseEntity<?> getStudentsBySchoolId(@RequestParam Long schoolId) {

        return ResponseEntity.ok(
                Map.of(
                        SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                        "data", studentService.getStudentsBySchoolId(schoolId)
                )
        );
    }

    // UPDATE STUDENT
    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long studentId,
            @RequestBody StudentRegistrationRequest request) {

        studentService.updateStudentDetails(studentId, request);
        return ResponseEntity.ok(
                Map.of(
                        SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                        SchoolConstants.MESSAGE, "Student updated successfully"
                )
        );
    }

    // DELETE STUDENT
    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {

        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(
                Map.of(
                        SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                        SchoolConstants.MESSAGE, "Student deleted successfully"
                )
        );
    }

    // STUDENT DASHBOARD APIs
    @GetMapping("/{studentId}/profile")
    public ResponseEntity<?> getStudentProfile(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentProfile(studentId));
    }

    @GetMapping("/{studentId}/attendance")
    public ResponseEntity<?> getStudentAttendance(@PathVariable Long studentId) {
        return ResponseEntity.ok(Map.of("data", "Attendance integration pending"));
    }

    @GetMapping("/{studentId}/grades")
    public ResponseEntity<?> getStudentGrades(@PathVariable Long studentId) {
        return ResponseEntity.ok(Map.of("data", "Grades integration pending"));
    }

    @GetMapping("/{studentId}/assignments")
    public ResponseEntity<?> getAssignments(@PathVariable Long studentId) {
        return ResponseEntity.ok(Map.of("data", "Assignments list"));
    }

    @PostMapping("/{studentId}/assignments/{assignmentId}/submit")
    public ResponseEntity<?> submitAssignment(
            @PathVariable Long studentId,
            @PathVariable Long assignmentId) {

        return ResponseEntity.ok(
                Map.of(SchoolConstants.MESSAGE, "Assignment submitted successfully")
        );
    }

    @GetMapping("/{studentId}/fees")
    public ResponseEntity<?> getFees(@PathVariable Long studentId) {
        return ResponseEntity.ok(Map.of("data", "Fee status"));
    }

    @GetMapping("/{studentId}/reports")
    public ResponseEntity<?> getReports(@PathVariable Long studentId) {
        return ResponseEntity.ok(Map.of("data", "Academic reports"));
    }

    @GetMapping("/{studentId}/certificates")
    public ResponseEntity<?> getCertificates(@PathVariable Long studentId) {
        return ResponseEntity.ok(Map.of("data", "Certificates"));
    }

    @PostMapping("/{studentId}/leave-request")
    public ResponseEntity<?> requestLeave(@PathVariable Long studentId) {
        return ResponseEntity.ok(
                Map.of(SchoolConstants.MESSAGE, "Leave request submitted")
        );
    }

    @GetMapping("/{studentId}/notifications")
    public ResponseEntity<?> getNotifications(@PathVariable Long studentId) {
        return ResponseEntity.ok(Map.of("data", "Notifications"));
    }

}


