package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.*;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Request.MarkAttendanceRequest;

import com.nimblix.SchoolPEPProject.Response.AttendanceReportResponse;
import com.nimblix.SchoolPEPProject.Response.AttendanceSummaryResponse;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;

import java.util.*;
import java.util.stream.Collectors;

import com.nimblix.SchoolPEPProject.Model.Student;



@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final DesignationRepository designationRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public void markStudentAttendance(MarkAttendanceRequest request) {
        if (attendanceRepository.existsByStudentIdAndAttendanceDate(
                request.getStudentId(), request.getDate())) {
            throw new IllegalArgumentException("Attendance already marked");
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(request.getStudentId());
        attendance.setAttendanceDate(request.getDate());
        attendance.setAttendanceStatus(request.getStatus());
        attendanceRepository.save(attendance);
    }

    @Override
    public AttendanceSummaryResponse getAverageAttendance(Long schoolId, String date) {
        long totalStudents = studentRepository.countBySchoolId(schoolId);
        long presentCount =
                attendanceRepository.countByAttendanceDateAndAttendanceStatus(
                        date, SchoolConstants.PRESENT);

        double percentage =
                totalStudents > 0 ? (presentCount * 100.0) / totalStudents : 0;

        AttendanceSummaryResponse response = new AttendanceSummaryResponse();
        response.setAttendancePercentage(formatPercentage(percentage));

        return response;
    }

    @Override
    public long getTotalPresentCount(Long schoolId, String date) {
        return attendanceRepository.countByAttendanceDateAndAttendanceStatus(
                date, SchoolConstants.PRESENT);
    }

    @Override
    public long getTotalAbsentCount(Long schoolId, String date) {
        return attendanceRepository.countByAttendanceDateAndAttendanceStatus(
                date, SchoolConstants.ABSENT);
    }

    @Override
    public List<AttendanceReportResponse> getAttendanceTrend(
            Long schoolId,
            String fromDate,
            String toDate) {

        List<Object[]> results =
                attendanceRepository.getAttendanceTrend(fromDate, toDate);

        List<AttendanceReportResponse> responseList = new ArrayList<>();

        for (Object[] row : results) {

            String date = (String) row[0];
            long total = (long) row[1];
            long present = (long) row[2];
            long absent = total - present;

            double percentage = total == 0 ? 0 : (present * 100.0) / total;

            responseList.add(
                    new AttendanceReportResponse(
                            date,
                            present,
                            absent,
                            formatPercentage(percentage)
                    )
            );
        }
        return responseList;
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

        if (request.getEmail() == null ||
                !request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid Email Format");
        }

        if (adminRepository.existsByEmailId(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Role role = roleRepository.findByRoleName(SchoolConstants.ADMIN_ROLE);

        Designation designation = designationRepository
                .findByDesignationName(SchoolConstants.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin designation not found"));

        Admin admin = new Admin();
        admin.setFirstName(request.getAdminFirstName());
        admin.setLastName(request.getAdminLastName());
        admin.setEmailId(request.getEmail());
        admin.setMobile(request.getAdminMobileNo());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(role);
        admin.setStaffType(StaffType.NON_TEACHING);
        admin.setDesignation(designation);
        admin.setStatus(SchoolConstants.STATUS);
        admin.setIsLogin(false);

        return adminRepository.save(admin).getId();
    }

    @Override
    public Map<String, Object> getClassWiseAttendance(Long schoolId, String date,
                                                      int page, int size,
                                                      String sortBy, String sortDir) {
        List<Student> students = studentRepository.findBySchoolId(schoolId);

        Map<String, List<Student>> groupedByClass = students.stream()
                .collect(Collectors.groupingBy(s -> s.getClassId() + "-" + s.getSection()));

        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Map.Entry<String, List<Student>> entry : groupedByClass.entrySet()) {
            String classKey = entry.getKey();
            List<Student> classStudents = entry.getValue();
            int total = classStudents.size();

            long presentCount = classStudents.stream()
                    .filter(s -> attendanceRepository.existsByStudentIdAndAttendanceDate(s.getId(), date))
                    .count();

            long absentCount = total - presentCount;
            double percentage = total == 0 ? 0 : (presentCount * 100.0) / total;

            Map<String, Object> classSummary = new HashMap<>();
            classSummary.put("class", classKey);
            classSummary.put("totalStudents", total);
            classSummary.put("present", presentCount);
            classSummary.put("absent", absentCount);
            classSummary.put("attendancePercentage", formatPercentage(percentage));

            responseList.add(classSummary);
        }

        // Sorting
        Comparator<Map<String, Object>> comparator = Comparator.comparing(m -> (Comparable) m.get(sortBy));
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }
        responseList.sort(comparator);

        // Pagination
        int start = page * size;
        int end = Math.min(start + size, responseList.size());
        List<Map<String, Object>> pagedList = responseList.subList(start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("data", pagedList);
        result.put("currentPage", page);
        result.put("pageSize", size);
        result.put("totalRecords", responseList.size());
        result.put("totalPages", (int) Math.ceil((double) responseList.size() / size));

        return result;
    }


    private double formatPercentage(double value) {
        if (value < 0) value = 0;
        if (value > 100) value = 100;
        return Math.round(value * 100.0) / 100.0; // round to 2 decimals
    }
}
