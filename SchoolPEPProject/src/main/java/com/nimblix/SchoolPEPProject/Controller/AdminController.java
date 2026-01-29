package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Request.FeesPaymentRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import com.nimblix.SchoolPEPProject.Service.FinanceService;
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
    private final FinanceService financeService;

    @PostMapping("/adminlogin")
    public ResponseEntity<String> submitEmail(@RequestBody String email) {
        return ResponseEntity.ok(adminService.submitEmail(email));
    }

    @PostMapping("/fees/pay")
    public ResponseEntity<?> payFees(@RequestBody FeesPaymentRequest request) {
        return ResponseEntity.ok(financeService.payFees(request));
    }
// added payment status
    @GetMapping("/fees/status")
    public ResponseEntity<?> getFeesStatus(@RequestParam Long studentId) {
        return ResponseEntity.ok(financeService.getFeesStatus(studentId));
    }

// added finace payment
    @GetMapping("/finance/fees/pending/total")
    public ResponseEntity<Double> getPendingFees(@RequestParam Long schoolId) {
        Double pendingFees = financeService.getTotalPendingFees(schoolId);
        return ResponseEntity.ok(pendingFees);
    }

    @PostMapping("/adminregister")
    public ResponseEntity<Map<String, String>> createAdminAccount(
            @RequestBody AdminAccountCreateRequest request) {

        Map<String, String> response = new HashMap<>();
        try {
            Long adminId = adminService.createAdminAccount(request);
            response.put(SchoolConstants.MESSAGE,
                    "Admin account created successfully. Admin ID: " + adminId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put(SchoolConstants.MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(SchoolConstants.MESSAGE, "Something went wrong");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/studentlist")
    public ResponseEntity<?> getStudentList(
            @RequestParam Long schoolId,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String status) {

        List<Student> students = adminService.getStudentList(
                schoolId, classId, section, status);

        if (students == null || students.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put(SchoolConstants.MESSAGE, "No students found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        Map<String, Object> success = new HashMap<>();
        success.put(SchoolConstants.MESSAGE, "Students fetched successfully");
        success.put(SchoolConstants.DATA, students);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/profile")
    public AdminProfileResponse getAdminProfile(
            @RequestParam Long adminId,
            @RequestParam Long schoolId) {

        return adminService.getAdminProfile(adminId, schoolId);
    }
}
