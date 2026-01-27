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
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    @Override
    public AdminProfileResponse getAdminProfile(Long adminId, Long schoolId) {

        Admin admin = adminRepository.findByIdAndSchoolId(adminId, schoolId);

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

    @Override
    public List<Map<String, Object>> getAcademicPerformanceTrend(Long schoolId, String month, Long classId, String section) {

        List<Student> students = studentRepository.findByAllFilters(schoolId, classId, section, "ACTIVE");
        if (students.isEmpty()) return new ArrayList<>();

        String ids = students.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.joining(","));

        String sql = "SELECT exam_date, marks_obtained, total_marks FROM academic_results " +
                "WHERE student_id IN (" + ids + ") AND exam_date LIKE '" + month + "%'";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

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
        if (students.isEmpty()) return new ArrayList<>();

        String ids = students.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.joining(","));

        String sql = "SELECT payment_date, amount_paid, total_fee FROM fee_payments " +
                "WHERE student_id IN (" + ids + ") AND payment_date LIKE '" + month + "%'";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

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

        if (students.isEmpty()) {
            summary.put("averageAttendancePercentage", 0);
            summary.put("totalPresent", 0);
            summary.put("totalAbsent", 0);
            summary.put("workingDays", 0);
            return summary;
        }

        List<Long> studentIds = students.stream()
                .map(Student::getId)
                .toList();

        List<Attendance> attendanceList = attendanceRepository.findAll()
                .stream()
                .filter(a -> studentIds.contains(a.getStudentId()))
                .filter(a -> a.getAttendanceDate().startsWith(month))
                .toList();

        if (attendanceList.isEmpty()) {
            summary.put("averageAttendancePercentage", 0);
            summary.put("totalPresent", 0);
            summary.put("totalAbsent", 0);
            summary.put("workingDays", 0);
            return summary;
        }

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
            Long schoolId,
            String month,
            Long classId,
            String section
    ) {

        List<Student> students = studentRepository.findByAllFilters(
                schoolId, classId, section, "ACTIVE"
        );

        if (students.isEmpty()) return new ArrayList<>();

        List<Long> studentIds = students.stream()
                .map(Student::getId)
                .toList();

        List<Attendance> attendanceList = attendanceRepository.findAll()
                .stream()
                .filter(a -> studentIds.contains(a.getStudentId()))
                .filter(a -> a.getAttendanceDate().startsWith(month))
                .toList();

        Map<String, List<Attendance>> grouped = attendanceList.stream()
                .collect(Collectors.groupingBy(Attendance::getAttendanceDate));

        List<Map<String, Object>> response = new ArrayList<>();

        for (String date : grouped.keySet().stream().sorted().toList()) {

            List<Attendance> dayRecords = grouped.get(date);

            long total = dayRecords.size();
            long present = dayRecords.stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getAttendanceStatus()))
                    .count();
            long absent = total - present;

            double percentage = total == 0 ? 0 : (present * 100.0) / total;

            Map<String, Object> map = new HashMap<>();
            map.put("date", date);
            map.put("totalStudents", total);
            map.put("presentCount", present);
            map.put("absentCount", absent);
            map.put("attendancePercentage", percentage);

            response.add(map);
        }

        return response;
    }


}

