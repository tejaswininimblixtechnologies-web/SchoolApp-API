package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AttendanceService {
    void markAttendance(Attendance attendance);

    double getAverageAttendance(Long schoolId, String date);

    long getPresentCount(Long schoolId, String date);

    long getAbsentCount(Long schoolId, String date);

    List<Object[]> getAttendanceTrend(Long schoolId, String fromDate, String toDate);

    Page<Object[]> getClassWiseAttendance(
            Long schoolId,
            String date,
            int page,
            int size,
            String sort
    );
}
