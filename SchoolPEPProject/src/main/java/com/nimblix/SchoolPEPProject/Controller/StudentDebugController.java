package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StudentDebugController {

    private final StudentRepository studentRepository;

    // ===== DEBUG ENDPOINTS =====

    @GetMapping("/check-students")
    public ResponseEntity<Map<String, Object>> checkStudents() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Count total students
            long totalStudents = studentRepository.count();
            response.put("totalStudents", totalStudents);
            
            // Check if any students exist
            if (totalStudents == 0) {
                response.put("message", "No students found in database");
                return ResponseEntity.ok(response);
            }
            
            // Get first student details
            var firstStudent = studentRepository.findAll().get(0);
            response.put("firstStudent", Map.of(
                "id", firstStudent.getId(),
                "email", firstStudent.getEmailId(),
                "status", firstStudent.getStatus(),
                "firstName", firstStudent.getFirstName(),
                "lastName", firstStudent.getLastName(),
                "role", firstStudent.getRole() != null ? firstStudent.getRole().getRoleName() : "NO_ROLE"
            ));
            
            response.put("message", "Students found");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create-test-student")
    public ResponseEntity<Map<String, Object>> createTestStudent() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if test student already exists
            if (studentRepository.existsByEmailId("student@test.com")) {
                response.put("message", "Test student already exists");
                return ResponseEntity.ok(response);
            }
            
            // This would need the full student creation logic
            response.put("message", "Please use /student/register to create test student");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ===== DATA FIX ENDPOINTS (Consolidated from StudentDataFixController) =====

    @PostMapping("/fix-student-status")
    public ResponseEntity<String> fixStudentStatus() {
        try {
            // Update all students with null or incorrect status to ACTIVE
            int updatedCount = studentRepository.fixStudentStatus(SchoolConstants.ACTIVE);
            return ResponseEntity.ok("Fixed " + updatedCount + " student records");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fixing student status: " + e.getMessage());
        }
    }

    // ===== ADDITIONAL ADMIN UTILITIES =====

    @GetMapping("/student-summary")
    public ResponseEntity<Map<String, Object>> getStudentSummary() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            long totalStudents = studentRepository.count();
            long activeStudents = studentRepository.countByStatus(SchoolConstants.ACTIVE);
            long inactiveStudents = totalStudents - activeStudents;
            
            response.put("totalStudents", totalStudents);
            response.put("activeStudents", activeStudents);
            response.put("inactiveStudents", inactiveStudents);
            response.put("message", "Student summary fetched successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
