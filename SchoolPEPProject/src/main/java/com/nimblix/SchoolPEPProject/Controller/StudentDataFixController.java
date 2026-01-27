package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StudentDataFixController {

    private final StudentRepository studentRepository;

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
}
