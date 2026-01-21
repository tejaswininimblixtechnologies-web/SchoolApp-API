package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Repository.AttendanceRepository;
import com.nimblix.SchoolPEPProject.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

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
            throw new RuntimeException("Attendance already marked for this student");
        }

        attendanceRepository.save(attendance);
    }

    // ===================== AVERAGE ATTENDANCE =====================
    @Override
    public double getAverageAttendance(Long schoolId, String date) {

        long total = attendanceRepository
                .countBySchoolIdAndAttendanceDate(schoolId, date);

        if (total == 0) return 0;

        long present = attendanceRepository
                .countBySchoolIdAndAttendanceDateAndAttendanceStatus(
                        schoolId, date, "PRESENT"
                );

        return formatPercentage((present * 100.0) / total);
    }

    // ===================== PRESENT COUNT =====================
    @Override
    public long getPresentCount(Long schoolId, String date) {
        return attendanceRepository
                .countBySchoolIdAndAttendanceDateAndAttendanceStatus(
                        schoolId, date, "PRESENT"
                );
    }

    // ===================== ABSENT COUNT =====================
    @Override
    public long getAbsentCount(Long schoolId, String date) {
        return attendanceRepository
                .countBySchoolIdAndAttendanceDateAndAttendanceStatus(
                        schoolId, date, "ABSENT"
                );
    }

    // ===================== ATTENDANCE TREND =====================
    @Override
    public List<Object[]> getAttendanceTrend(Long schoolId, String fromDate, String toDate) {

        // 🔹 Fetch raw data: date, present count, total count
        List<Object[]> rawData = attendanceRepository.getAttendanceTrend(schoolId, fromDate, toDate);

        // 🔹 Transform counts into percentage
        List<Object[]> trend = new ArrayList<>();
        for (Object[] row : rawData) {

            String date = (String) row[0];
            Long present = ((Number) row[1]).longValue();
            Long total = ((Number) row[2]).longValue();

            double percentage = 0.0;
            if (total > 0) {
                percentage = (present * 100.0) / total;
            }

            // Round and clamp
            percentage = formatPercentage(percentage);

            trend.add(new Object[]{date, percentage});
        }

        return trend;
    }

    // ===================== CLASS-WISE ATTENDANCE =====================
    @Override
    public Page<Object[]> getClassWiseAttendance(Long schoolId, String date, int page, int size, String sort) {

        Sort sortOrder = Sort.unsorted();
        if ("attendanceDesc".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "attendanceStatus");
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        return attendanceRepository.getClassWiseAttendance(schoolId, date, pageable);
    }

    // ===================== HELPER METHOD =====================
    private double formatPercentage(double value) {
        if (value < 0) return 0;
        if (value > 100) return 100;
        return Math.round(value * 100.0) / 100.0;
    }
}
