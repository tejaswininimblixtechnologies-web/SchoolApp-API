package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Response.AttendanceTrendResponse;
import com.nimblix.SchoolPEPProject.Response.ClassWiseAttendanceResponse;
import com.nimblix.SchoolPEPProject.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // ===================== MARK =====================
    @PostMapping("/mark")
    public Map<String, String> markAttendance(@RequestBody Attendance attendance) {
        attendanceService.markAttendance(attendance);
        return Map.of("message", "Attendance marked successfully");
    }

    // ===================== AVERAGE =====================
    @GetMapping("/average")
    public Map<String, Double> getAverageAttendance(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        LocalDate localDate = LocalDate.parse(date);
        double avg = attendanceService.getAverageAttendance(schoolId, localDate);
        return Map.of("averageAttendance", avg);
    }

    // ===================== PRESENT =====================
    @GetMapping("/present/count")
    public Map<String, Long> getPresentCount(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        LocalDate localDate = LocalDate.parse(date);
        return Map.of(
                "presentCount",
                attendanceService.getPresentCount(schoolId, localDate)
        );
    }

    // ===================== ABSENT =====================
    @GetMapping("/absent/count")
    public Map<String, Long> getAbsentCount(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        LocalDate localDate = LocalDate.parse(date);
        return Map.of(
                "absentCount",
                attendanceService.getAbsentCount(schoolId, localDate)
        );
    }

    // ===================== TREND =====================
    @GetMapping("/trend")
    public List<AttendanceTrendResponse> getAttendanceTrend(
            @RequestParam Long schoolId,
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return attendanceService.getAttendanceTrend(
                schoolId,
                LocalDate.parse(fromDate),
                LocalDate.parse(toDate)
        );
    }

    // ===================== CLASS WISE =====================
    @GetMapping("/class-wise")
    public Page<ClassWiseAttendanceResponse> getClassWiseAttendance(
            @RequestParam Long schoolId,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        return attendanceService.getClassWiseAttendance(
                schoolId,
                LocalDate.parse(date),
                page,
                size,
                sort
        );
    }
}

