package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Response.AttendanceTrendResponse;
import com.nimblix.SchoolPEPProject.Response.ClassWiseAttendanceResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    void markAttendance(Attendance attendance);

    double getAverageAttendance(Long schoolId, LocalDate date);

    long getPresentCount(Long schoolId, LocalDate date);

    long getAbsentCount(Long schoolId, LocalDate date);

    List<AttendanceTrendResponse> getAttendanceTrend(
            Long schoolId,
            LocalDate fromDate,
            LocalDate toDate
    );

    Page<ClassWiseAttendanceResponse> getClassWiseAttendance(
            Long schoolId,
            LocalDate date,
            int page,
            int size,
            String sort
    );
}
