package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Model.AttendanceStatus;
import com.nimblix.SchoolPEPProject.Repository.AttendanceRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Response.AttendanceTrendResponse;
import com.nimblix.SchoolPEPProject.Response.ClassWiseAttendanceResponse;
import com.nimblix.SchoolPEPProject.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ===================== MARK ATTENDANCE =====================
    @Override
    public void markAttendance(Attendance attendance) {

        boolean exists = attendanceRepository
                .existsBySchoolIdAndStudentIdAndAttendanceDate(
                        attendance.getSchoolId(),
                        attendance.getStudentId(),
                        attendance.getAttendanceDate()
                );

        if (exists) {
            throw new RuntimeException("Attendance already marked for this student and date");
        }

        attendanceRepository.save(attendance);
    }

    // ===================== AVERAGE ATTENDANCE =====================
    @Override
    public double getAverageAttendance(Long schoolId, LocalDate date) {

        long presentCount = attendanceRepository
                .countBySchoolIdAndAttendanceDateAndAttendanceStatus(
                        schoolId,
                        date,
                        AttendanceStatus.PRESENT
                );

        long totalStudents = studentRepository.countBySchoolId(schoolId);

        return calculatePercentage(presentCount, totalStudents);
    }

    // ===================== PRESENT COUNT =====================
    @Override
    public long getPresentCount(Long schoolId, LocalDate date) {
        return attendanceRepository
                .countBySchoolIdAndAttendanceDateAndAttendanceStatus(
                        schoolId,
                        date,
                        AttendanceStatus.PRESENT
                );
    }

    // ===================== ABSENT COUNT =====================
    @Override
    public long getAbsentCount(Long schoolId, LocalDate date) {
        return attendanceRepository
                .countBySchoolIdAndAttendanceDateAndAttendanceStatus(
                        schoolId,
                        date,
                        AttendanceStatus.ABSENT
                );
    }

    // ===================== ATTENDANCE TREND =====================
    @Override
    public List<AttendanceTrendResponse> getAttendanceTrend(
            Long schoolId,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        List<Object[]> rawData =
                attendanceRepository.getAttendanceTrend(schoolId, fromDate, toDate);

        List<AttendanceTrendResponse> response = new ArrayList<>();

        for (Object[] row : rawData) {

            LocalDate date = (LocalDate) row[0];
            long present = ((Number) row[1]).longValue();
            long total = ((Number) row[2]).longValue();
            long absent = total - present;

            double percentage = calculatePercentage(present, total);

            response.add(new AttendanceTrendResponse(
                    date,
                    present,
                    absent,
                    percentage
            ));
        }

        return response;
    }

    // ===================== CLASS-WISE ATTENDANCE =====================
    @Override
    public Page<ClassWiseAttendanceResponse> getClassWiseAttendance(
            Long schoolId,
            LocalDate date,
            int page,
            int size,
            String sort
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> rawPage =
                attendanceRepository.getClassWiseAttendance(schoolId, date, pageable);

        List<ClassWiseAttendanceResponse> responseList = new ArrayList<>();

        for (Object[] row : rawPage.getContent()) {

            Long classId = ((Number) row[0]).longValue();
            String section = (String) row[1];
            long totalAttendance = ((Number) row[2]).longValue();
            long present = ((Number) row[3]).longValue();
            long absent = ((Number) row[4]).longValue();

            long totalStudents =
                    studentRepository.countBySchoolIdAndClassIdAndSection(
                            schoolId, classId, section
                    );

            double percentage = calculatePercentage(present, totalStudents);

            responseList.add(new ClassWiseAttendanceResponse(
                    classId,
                    section,
                    totalStudents,
                    present,
                    absent,
                    percentage
            ));
        }

        // 🔹 Sorting by attendance %
        if ("attendanceDesc".equalsIgnoreCase(sort)) {
            responseList.sort(
                    Comparator.comparing(ClassWiseAttendanceResponse::getAttendancePercentage).reversed()
            );
        } else if ("attendanceAsc".equalsIgnoreCase(sort)) {
            responseList.sort(
                    Comparator.comparing(ClassWiseAttendanceResponse::getAttendancePercentage)
            );
        }

        return new PageImpl<>(responseList, pageable, rawPage.getTotalElements());
    }

    // ===================== PERCENTAGE FORMAT (TASK 7) =====================
    private double calculatePercentage(long present, long total) {
        if (total == 0) {
            return 0.0;
        }

        double percentage = (present * 100.0) / total;

        percentage = Math.min(100, Math.max(0, percentage));

        // 🔹 1 decimal precision
        return Math.round(percentage * 10.0) / 10.0;
    }
}
