package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.AdminRepository;
import com.nimblix.SchoolPEPProject.Repository.DesignationRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
//import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import com.nimblix.SchoolPEPProject.Request.AdminProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.nimblix.SchoolPEPProject.Repository.AttendanceRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final DesignationRepository designationRepository;
    private final JdbcTemplate jdbcTemplate;
    private final AttendanceRepository attendanceRepository;

    @Override
    public String submitEmail(String email) {

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (adminRepository.existsByEmailId(email)) {
            return "Email already registered.";
        }

        return "Email accepted. Continue to account creation.";
    }

    @Override
    public Long createAdminAccount(AdminAccountCreateRequest request) {

        // Email validation
        if (request.getEmail() == null ||
                !request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid Email Format");
        }

        if (adminRepository.existsByEmailId(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Mobile validation
        if (request.getAdminMobileNo() == null || request.getAdminMobileNo().length() != 10) {
            throw new IllegalArgumentException("Invalid mobile number (must be 10 digits)");
        }

        if (adminRepository.existsByMobile(request.getAdminMobileNo())) {
            throw new IllegalArgumentException("Mobile number already registered");
        }

        // Password validation
        if (!request.getPassword().equals(request.getReEnterPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Role role = roleRepository.findByRoleName(SchoolConstants.ADMIN_ROLE);

        Designation adminDesignation =
                designationRepository
                        .findByDesignationName(SchoolConstants.ADMIN)
                        .orElseThrow(() ->
                                new RuntimeException("Admin designation not found"));

        Admin admin = new Admin();
        admin.setFirstName(request.getAdminFirstName());
        admin.setLastName(request.getAdminLastName());
        admin.setEmailId(request.getEmail());
        admin.setMobile(request.getAdminMobileNo());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        admin.setRole(role);
        admin.setStaffType(StaffType.NON_TEACHING);
        admin.setDesignation(adminDesignation);
        admin.setStatus(SchoolConstants.STATUS);
        admin.setIsLogin(false);

        Admin savedAdmin = adminRepository.save(admin);

        return savedAdmin.getId();
    }

    @Override
    public List<Student> getStudentList(
            Long schoolId,
            Long classId,
            String section,
            String status
    ) {
        return studentRepository.findByAllFilters(
                schoolId,
                classId,
                section,
                status
        );
    }

    // Fetches profile details of the currently logged-in admin only
    @Override
    public AdminProfileResponse getLoggedInAdminProfile() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        Admin admin = adminRepository.findByEmailId(email)
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        AdminProfileResponse response = new AdminProfileResponse();
        response.setAdminId(admin.getId());
        response.setUserId(admin.getId());
        response.setFirstName(admin.getFirstName());
        response.setLastName(admin.getLastName());
        response.setEmailId(admin.getEmailId());
        response.setMobile(admin.getMobile());
        response.setGender(admin.getGender());
        response.setDesignation(admin.getDesignation());
        response.setProfilePicture(admin.getProfilePicture());
        response.setSchoolId(admin.getSchoolId());

        return response;
    }

    /**
     * Updates profile details of the currently logged-in admin.
     * Supports partial updates.
     * Email field is intentionally not updatable.
     * Mobile number must be 10 digits.
     */
    @Override
    public void updateLoggedInAdminProfile(AdminProfileUpdateRequest request) {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Admin admin = adminRepository.findByEmailId(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        if (request.getFirstName() != null)
            admin.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            admin.setLastName(request.getLastName());

        if (request.getMobile() != null) {
            if (!request.getMobile().matches("\\d{10}"))
                throw new IllegalArgumentException("Invalid mobile number");
            admin.setMobile(request.getMobile());
        }

        if (request.getProfilePicture() != null)
            admin.setProfilePicture(request.getProfilePicture());

        adminRepository.save(admin);
    }

    /**
     * Soft Delete Admin Profile
     *
     * Deactivates the logged-in admin account.
     * - Sets status to IN_ACTIVE
     * - Invalidates active login session
     * - Hard delete is NOT performed
     *
     * This ensures audit safety and prevents future login.
     */
    @Override
    public void softDeleteLoggedInAdmin() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Admin admin = adminRepository.findByEmailId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setStatus(SchoolConstants.IN_ACTIVE);
        admin.setIsLogin(false);

        adminRepository.save(admin);
    }

    //........
    @Override
    public List<Map<String, Object>> getAcademicPerformanceTrend(Long schoolId, String month, Long classId, String section) {

        List<Student> students = studentRepository.findByAllFilters(schoolId, classId, section, "ACTIVE");
        if (students.isEmpty()) throw new IllegalArgumentException("No active students found for given filters");
        String ids = students.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.joining(","));
        String sql = "SELECT exam_date, marks_obtained, total_marks FROM academic_results " +
                "WHERE student_id IN (" + ids + ") AND exam_date LIKE '" + month + "%'";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        if (results.isEmpty()) throw new IllegalArgumentException("No academic performance records found for selected month");
        Map<String, List<Map<String, Object>>> grouped =
                results.stream().collect(Collectors.groupingBy(r -> r.get("exam_date").toString()));

        List<Map<String, Object>> response = new ArrayList<>();
        for (String date : grouped.keySet()) {
            List<Map<String, Object>> exams = grouped.get(date);
            double avg = exams.stream()
                    .mapToDouble(r -> ((Number) r.get("marks_obtained")).doubleValue() /
                            ((Number) r.get("total_marks")).doubleValue() * 100)
                    .average().orElse(0);

            Map<String, Object> map = new HashMap<>();
            map.put("examDate", date);
            map.put("averageScore", avg);
            response.add(map);
        }
        return response;
    }
    @Override
    public List<Map<String, Object>> getFeeCollectionTrend(Long schoolId, String month, Long classId, String section) {
        List<Student> students = studentRepository.findByAllFilters(schoolId, classId, section, "ACTIVE");
        if (students.isEmpty())  throw new IllegalArgumentException("No active students found for given filters");
        String ids = students.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.joining(","));
        String sql = "SELECT payment_date, amount_paid, total_fee FROM fee_payments " +
                "WHERE student_id IN (" + ids + ") AND payment_date LIKE '" + month + "%'";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        if (results.isEmpty()) throw new IllegalArgumentException("No fee payment records found for selected month");


        Map<String, List<Map<String, Object>>> grouped =
                results.stream().collect(Collectors.groupingBy(r -> r.get("payment_date").toString()));

        List<Map<String, Object>> response = new ArrayList<>();

        for (String date : grouped.keySet()) {
            List<Map<String, Object>> payments = grouped.get(date);
            double collected = payments.stream()
                    .mapToDouble(r -> ((Number) r.get("amount_paid")).doubleValue()).sum();
            Map<String, Object> map = new HashMap<>();
            map.put("date", date);
            map.put("totalCollected", collected);
            response.add(map);
        }
        return response;
    }

    @Override
    public Map<String, Object> getFeeCollectionSummary(Long schoolId, String month, Long classId, String section) {
        List<Map<String, Object>> trend = getFeeCollectionTrend(schoolId, month, classId, section);
        if (trend.isEmpty()) throw new IllegalArgumentException("No fee data available to summarize");
        double total = trend.stream()
                .mapToDouble(t -> ((Number) t.get("totalCollected")).doubleValue())
                .sum();

        Map<String, Object> map = new HashMap<>();
        map.put("totalFeeCollected", total);
        map.put("daysCount", trend.size());
        return map;
    }
    @Override
    public Map<String, Object> getAttendanceSummaryAnalytics(
            Long schoolId,
            String month,
            Long classId,
            String section
    ) {

        List<Student> students = studentRepository.findByAllFilters(
                schoolId, classId, section, "ACTIVE"
        );

        Map<String, Object> summary = new HashMap<>();

        if (students.isEmpty()) throw new IllegalArgumentException("No active students found for given filters");

        List<Long> studentIds = students.stream()
                .map(Student::getId)
                .toList();

        List<Attendance> attendanceList = attendanceRepository.findAll()
                .stream()
                .filter(a -> studentIds.contains(a.getStudentId()))
                .filter(a -> a.getAttendanceDate().startsWith(month))
                .toList();

        if (attendanceList.isEmpty()) throw new IllegalArgumentException("No attendance data found for selected month");

        Map<String, List<Attendance>> grouped = attendanceList.stream()
                .collect(Collectors.groupingBy(Attendance::getAttendanceDate));

        int workingDays = grouped.size();
        long totalPresent = 0;
        long totalAbsent = 0;

        for (List<Attendance> day : grouped.values()) {
            long present = day.stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getAttendanceStatus()))
                    .count();
            totalPresent += present;
            totalAbsent += day.size() - present;
        }

        double avgPercentage = (totalPresent + totalAbsent) == 0 ? 0 :
                (totalPresent * 100.0) / (totalPresent + totalAbsent);

        summary.put("averageAttendancePercentage", avgPercentage);
        summary.put("totalPresent", totalPresent);
        summary.put("totalAbsent", totalAbsent);
        summary.put("workingDays", workingDays);
        return summary;
    }

    @Override
    public List<Map<String, Object>> getAttendanceTrendAnalytics(
            Long schoolId, String month, Long classId, String section) {
        if (schoolId == null)
            throw new IllegalArgumentException("School ID is required");

        if (month == null || month.isBlank())
            throw new IllegalArgumentException("Month is required (format: YYYY-MM)");

        List<Student> students = studentRepository.findByAllFilters(schoolId, classId, section, "ACTIVE");

        if (students.isEmpty())
            throw new IllegalArgumentException("No active students found for given filters");

        List<Long> studentIds = students.stream().map(Student::getId).toList();

        List<Attendance> attendanceList = attendanceRepository.findAll().stream()
                .filter(a -> studentIds.contains(a.getStudentId()))
                .filter(a -> a.getAttendanceDate().startsWith(month))
                .toList();

        if (attendanceList.isEmpty())
            throw new IllegalArgumentException("No attendance records found for selected month");

        Map<String, List<Attendance>> grouped = attendanceList.stream()
                .collect(Collectors.groupingBy(Attendance::getAttendanceDate));

        List<Map<String, Object>> response = new ArrayList<>();

        for (String date : grouped.keySet().stream().sorted().toList()) {
            List<Attendance> dayRecords = grouped.get(date);

            long total = dayRecords.size();
            long present = dayRecords.stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getAttendanceStatus()))
                    .count();

            Map<String, Object> map = new HashMap<>();
            map.put("date", date);
            map.put("attendancePercentage", (present * 100.0) / total);
            response.add(map);
        }
        return response;
    }

}

