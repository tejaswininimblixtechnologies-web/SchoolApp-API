package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    @PostMapping("/mark")
    public Map<String, String> markAttendance(@RequestBody Attendance attendance) {
        attendanceService.markAttendance(attendance);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Attendance marked successfully");
        return response;
    }
    @GetMapping("/average")
    public Map<String, Double> getAverageAttendance(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        double average = attendanceService.getAverageAttendance(schoolId, date);
        Map<String, Double> response = new HashMap<>();
        response.put("averageAttendance", average);
        return response;
    }
    @GetMapping("/present/count")
    public Map<String, Long> getPresentCount(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        Map<String, Long> response = new HashMap<>();
        response.put("presentCount",
                attendanceService.getPresentCount(schoolId, date));
        return response;
    }
    @GetMapping("/absent/count")
    public Map<String, Long> getAbsentCount(
            @RequestParam Long schoolId,
            @RequestParam String date
    ) {
        Map<String, Long> response = new HashMap<>();
        response.put("absentCount",
                attendanceService.getAbsentCount(schoolId, date));
        return response;
    }

    @GetMapping("/trend")
    public List<Object[]> getAttendanceTrend(
            @RequestParam Long schoolId,
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return attendanceService
                .getAttendanceTrend(schoolId, fromDate, toDate);
    }
    @GetMapping("/class-wise")
    public Page<Object[]> getClassWiseAttendance(
            @RequestParam Long schoolId,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        return attendanceService
                .getClassWiseAttendance(schoolId, date, page, size, sort);
    }
}
