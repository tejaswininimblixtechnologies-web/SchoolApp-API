package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Request.CreateNoticeRequest;
import com.nimblix.SchoolPEPProject.Response.*;
import com.nimblix.SchoolPEPProject.Service.AdminDashboardService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Override
    public TeacherCountResponse getTeacherCount(Long schoolId) {
        return new TeacherCountResponse(85, "+5");
    }

    @Override
    public StudentCountResponse getStudentCount(Long schoolId) {
        return new StudentCountResponse(1350, "+12");
    }

    @Override
    public TransportCountResponse getTransportCount(Long schoolId) {
        return new TransportCountResponse(12, 12);
    }

    @Override
    public RevenueResponse getRevenue(Long schoolId) {
        return new RevenueResponse(538000, "+8.2");
    }

    @Override
    public List<LiveAttendanceResponse> getLiveAttendance(Long schoolId) {
        return List.of(
                new LiveAttendanceResponse("Class 1-A", "Mathematics", "09:00 AM", 28, 32, 88)
        );
    }

    @Override
    public OnDutyTeacherResponse getOnDutyTeachers(Long schoolId) {
        return new OnDutyTeacherResponse(45);
    }

    @Override
    public List<TeacherScheduleResponse> getTeacherTodaySchedule(Long schoolId) {
        return List.of(
                new TeacherScheduleResponse("Sarah Johnson", "Class 3-A", "09:00", "11:00")
        );
    }

    @Override
    public PendingReportsResponse getPendingReports(Long schoolId) {
        return new PendingReportsResponse(8);
    }

    @Override
    public List<ActiveDriversResponse> getActiveDrivers(Long schoolId) {
        return List.of(
                new ActiveDriversResponse("John Driver", "Route A", "ACTIVE", "07:00 AM")
        );
    }

    @Override
    public List<NoticeResponse> getNotices(Long schoolId) {
        return List.of(
                new NoticeResponse("Parent-Teacher Meeting", "2026-01-20", "INFO")
        );
    }

    @Override
    public NoticeResponse createNotice(CreateNoticeRequest request) {
        return new NoticeResponse(request.getTitle(), request.getDate(), "INFO");
    }

    @Override
    public List<CalendarEventResponse> getCalendarEvents(Long schoolId, int month, int year) {
        return List.of(
                new CalendarEventResponse("2026-01-18", "Mid-term Exams")
        );
    }

    @Override
    public ChartResponse getAttendanceReport(Long schoolId, String range) {
        return new ChartResponse(
                Arrays.asList("Week 1", "Week 2", "Week 3", "Week 4"),
                Arrays.asList(85.0, 88.0, 90.0, 87.0)
        );
    }

    @Override
    public ChartResponse getRevenueReport(Long schoolId, String range) {
        return new ChartResponse(
                Arrays.asList("Jan", "Feb", "Mar"),
                Arrays.asList(120000.0, 150000.0, 268000.0)
        );
    }

    @Override
    public ChartResponse getStudentsGrowthReport(Long schoolId, String range) {
        return new ChartResponse(
                Arrays.asList("Jan", "Feb", "Mar"),
                Arrays.asList(120.0, 150.0, 200.0)
        );
    }
}
