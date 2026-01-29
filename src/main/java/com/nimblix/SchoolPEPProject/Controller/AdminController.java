package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import com.nimblix.SchoolPEPProject.Request.AdminProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/adminlogin")
    public ResponseEntity<String> submitEmail(@RequestBody String email) {
        String response = adminService.submitEmail(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/adminregister")
    public ResponseEntity<Map<String, String>> createAdminAccount(@RequestBody AdminAccountCreateRequest request) {
        Map<String, String> response = new HashMap<>();

        try {
            Long adminId = adminService.createAdminAccount(request);
            response.put(SchoolConstants.MESSAGE, "Admin account created successfully. Admin ID: " + adminId);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put(SchoolConstants.MESSAGE, "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("message", "Error: Something went wrong. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/studentlist")
    public ResponseEntity<?> getStudentList(
            @RequestParam Long schoolId,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String status
    ) {
        try {
            List<Student> students = adminService.getStudentList(
                    schoolId,
                    classId,
                    section,
                    status
            );

            if (students == null || students.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put(SchoolConstants.MESSAGE, "No students found for the given filters");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            Map<String, Object> success = new HashMap<>();
            success.put(SchoolConstants.MESSAGE, "Students fetched successfully");
            success.put(SchoolConstants.DATA, students);
            return ResponseEntity.ok(success);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(SchoolConstants.MESSAGE, "Failed to fetch student list: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    //ADMIN PROFILE MANAGEMENT APIs
    @GetMapping("/profile")
    public ResponseEntity<AdminProfileResponse> getAdminProfile() {
        return ResponseEntity.ok(adminService.getLoggedInAdminProfile());
    }

    // UPDATE ADMIN PROFILE API
    // Allows logged-in admin to update profile details.
    // Editable fields: firstName, lastName, mobile, profilePicture
    // Email update is NOT allowed.
    @PutMapping("/profile")
    public ResponseEntity<?> updateAdminProfile(
            @RequestBody AdminProfileUpdateRequest request
    ) {
        adminService.updateLoggedInAdminProfile(request);
        return ResponseEntity.ok(
                Map.of("message", "Profile updated successfully")
        );
    }

    /**
     *Delete (Soft Delete) Admin Profile
     *
     * Deactivates admin account instead of hard deletion.
     * Account status is set to DELETED.
     */
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteAdminProfile() {
        adminService.softDeleteLoggedInAdmin();
        return ResponseEntity.ok(
                Map.of("message", "Admin profile deactivated successfully")
        );
    }

    @GetMapping("/attendance/analytics")
    public ResponseEntity<?> getAttendanceAnalytics(
            @RequestParam Long schoolId,
            @RequestParam String month,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section) {

        return ResponseEntity.ok(Map.of(
                "message", "Attendance analytics fetched successfully",
                "data", adminService.getAttendanceTrendAnalytics(schoolId, month, classId, section)
        ));
    }

    @GetMapping("/attendance/summary")
    public ResponseEntity<?> getAttendanceSummary(
            @RequestParam Long schoolId,
            @RequestParam String month,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section) {

        return ResponseEntity.ok(Map.of(
                "message", "Attendance summary fetched successfully",
                "data", adminService.getAttendanceSummaryAnalytics(schoolId, month, classId, section)
        ));
    }

    @GetMapping("/academic-performance/trend")
    public ResponseEntity<?> getAcademicTrend(
            @RequestParam Long schoolId,
            @RequestParam String month,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section) {

        return ResponseEntity.ok(Map.of(
                "message", "Academic performance trend fetched successfully",
                "data", adminService.getAcademicPerformanceTrend(schoolId, month, classId, section)
        ));
    }

    @GetMapping("/fee/trend")
    public ResponseEntity<?> getFeeTrend(
            @RequestParam Long schoolId,
            @RequestParam String month,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section) {

        return ResponseEntity.ok(Map.of(
                "message", "Fee collection trend fetched successfully",
                "data", adminService.getFeeCollectionTrend(schoolId, month, classId, section)
        ));
    }

    @GetMapping("/fee/summary")
    public ResponseEntity<?> getFeeSummary(
            @RequestParam Long schoolId,
            @RequestParam String month,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section) {

        return ResponseEntity.ok(Map.of(
                "message", "Fee collection summary fetched successfully",
                "data", adminService.getFeeCollectionSummary(schoolId, month, classId, section)
        ));
    }

    @GetMapping("/fee/analytics")
    public ResponseEntity<?> getFeeAnalytics(
            @RequestParam Long schoolId,
            @RequestParam String month,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section) {

        return ResponseEntity.ok(Map.of(
                "message", "Fee analytics fetched successfully",
                "trend", adminService.getFeeCollectionTrend(schoolId, month, classId, section),
                "summary", adminService.getFeeCollectionSummary(schoolId, month, classId, section)
        ));
    }
}



