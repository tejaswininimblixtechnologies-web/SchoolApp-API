package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Request.MarkAttendanceRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nimblix.SchoolPEPProject.Response.AttendanceReportResponse;


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


    @GetMapping("/profile")
    public AdminProfileResponse getAdminProfile(
            @RequestParam Long adminId,
            @RequestParam Long schoolId
    ) {
        return adminService.getAdminProfile(adminId, schoolId);
    }

    // Endpoint to mark student attendance

    @PostMapping("/attendance/mark")
    public ResponseEntity<?> markAttendance(
            @RequestBody MarkAttendanceRequest request
    ) {
        try {
            adminService.markStudentAttendance(request);
            return ResponseEntity.ok(
                    Map.of(SchoolConstants.MESSAGE, "Attendance marked successfully")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(SchoolConstants.MESSAGE, e.getMessage()));
        }

    }



    // Endpoint to get average attendance percentage

    @GetMapping("/attendance/average")
    public ResponseEntity<?> getAverageAttendance(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        return ResponseEntity.ok(
                adminService.getAverageAttendance(schoolId, date)
        );
    }



    // Endpoint to get total present count

    @GetMapping("/attendance/present/count")
    public ResponseEntity<?> getTotalPresentCount(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        try {
            long presentCount = adminService.getTotalPresentCount(schoolId, date);
            return ResponseEntity.ok(
                    Map.of(
                            "schoolId", schoolId,
                            "date", date,
                            "presentCount", presentCount
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to fetch present count: " + e.getMessage()));
        }
    }
    // Endpoint to get total absent count

    @GetMapping("/attendance/absent/count")
    public ResponseEntity<?> getTotalAbsentCount(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        long count = adminService.getTotalAbsentCount(schoolId, date);
        return ResponseEntity.ok(Map.of("totalAbsent", count));
    }
    @GetMapping("/attendance/trend")
    public ResponseEntity<List<AttendanceReportResponse>> getAttendanceTrend(
            @RequestParam Long schoolId,
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return ResponseEntity.ok(
                adminService.getAttendanceTrend(
                        schoolId,
                        fromDate,
                        toDate
                )
        );
    }


    // Endpoint to get class-wise attendance with pagination and sorting
    @GetMapping("/attendance/class-wise")
    public ResponseEntity<?> getClassWiseAttendance(
            @RequestParam Long schoolId,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "class") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        try {
            return ResponseEntity.ok(
                    adminService.getClassWiseAttendance(schoolId, date, page, size, sortBy, sortDir)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to fetch class-wise attendance: " + e.getMessage()));
        }
    }



}



