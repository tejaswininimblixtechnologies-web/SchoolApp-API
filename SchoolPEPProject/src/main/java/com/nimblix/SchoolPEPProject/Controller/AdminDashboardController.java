package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.CreateNoticeRequest;
import com.nimblix.SchoolPEPProject.Response.*;
import com.nimblix.SchoolPEPProject.Service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    // 1.1 Total Teachers
    @GetMapping("/dashboard/teachers/count")
    public TeacherCountResponse getTotalTeachersCount(@RequestParam Long schoolId) {
        return adminDashboardService.getTeacherCount(schoolId);
    }

    // 1.2 Total Students
    @GetMapping("/dashboard/students/count")
    public StudentCountResponse getTotalStudentsCount(@RequestParam Long schoolId) {
        return adminDashboardService.getStudentCount(schoolId);
    }

    // 1.3 Vehicles / Drivers
    @GetMapping("/dashboard/transport/count")
    public TransportCountResponse getTransportCounts(@RequestParam Long schoolId) {
        return adminDashboardService.getTransportCount(schoolId);
    }

    // 1.4 Revenue
    @GetMapping("/dashboard/revenue")
    public RevenueResponse getTotalRevenue(@RequestParam Long schoolId) {
        return adminDashboardService.getRevenue(schoolId);
    }

    // 2.1 Live Attendance
    @GetMapping("/attendance/live")
    public List<LiveAttendanceResponse> getLiveAttendance(@RequestParam Long schoolId) {
        return adminDashboardService.getLiveAttendance(schoolId);
    }

    // 3.1 On Duty Teachers
    @GetMapping("/teachers/on-duty")
    public OnDutyTeacherResponse getOnDutyTeachers(@RequestParam Long schoolId) {
        return adminDashboardService.getOnDutyTeachers(schoolId);
    }

    // 3.2 Teachers Schedule Today
    @GetMapping("/teachers/today-schedule")
    public List<TeacherScheduleResponse> getTeachersTodaySchedule(@RequestParam Long schoolId) {
        return adminDashboardService.getTeacherTodaySchedule(schoolId);
    }

    // 3.3 Pending Reports
    @GetMapping("/reports/pending/count")
    public PendingReportsResponse getPendingReports(@RequestParam Long schoolId) {
        return adminDashboardService.getPendingReports(schoolId);
    }

    // 4.1 Active Drivers
    @GetMapping("/transport/drivers")
    public List<ActiveDriversResponse> getActiveDrivers(@RequestParam Long schoolId) {
        return adminDashboardService.getActiveDrivers(schoolId);
    }

    // 5.1 Notice List
    @GetMapping("/notices")
    public List<NoticeResponse> getNotices(@RequestParam Long schoolId) {
        return adminDashboardService.getNotices(schoolId);
    }

    // 5.2 Create Notice
    @PostMapping("/notices")
    public NoticeResponse createNotice(@RequestBody CreateNoticeRequest request) {
        return adminDashboardService.createNotice(request);
    }

    // 6.1 Calendar Events
    @GetMapping("/calendar/events")
    public List<CalendarEventResponse> getCalendarEvents(
            @RequestParam Long schoolId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return adminDashboardService.getCalendarEvents(schoolId, month, year);
    }

    // 7.1 Attendance report chart
    @GetMapping("/reports/attendance")
    public ChartResponse getAttendanceReport(@RequestParam Long schoolId, @RequestParam String range) {
        return adminDashboardService.getAttendanceReport(schoolId, range);
    }

    // 7.2 Revenue report chart
    @GetMapping("/reports/revenue")
    public ChartResponse getRevenueReport(@RequestParam Long schoolId, @RequestParam String range) {
        return adminDashboardService.getRevenueReport(schoolId, range);
    }

    // 7.3 Students Growth report chart
    @GetMapping("/reports/students-growth")
    public ChartResponse getStudentsGrowthReport(@RequestParam Long schoolId, @RequestParam String range) {
        return adminDashboardService.getStudentsGrowthReport(schoolId, range);
    }
}
