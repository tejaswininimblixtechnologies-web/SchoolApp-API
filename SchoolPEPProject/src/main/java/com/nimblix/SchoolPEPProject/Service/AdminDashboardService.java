package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.CreateNoticeRequest;
import com.nimblix.SchoolPEPProject.Response.*;

import java.util.List;

public interface AdminDashboardService {

    TeacherCountResponse getTeacherCount(Long schoolId);

    StudentCountResponse getStudentCount(Long schoolId);

    TransportCountResponse getTransportCount(Long schoolId);

    RevenueResponse getRevenue(Long schoolId);

    List<LiveAttendanceResponse> getLiveAttendance(Long schoolId);

    OnDutyTeacherResponse getOnDutyTeachers(Long schoolId);

    List<TeacherScheduleResponse> getTeacherTodaySchedule(Long schoolId);

    PendingReportsResponse getPendingReports(Long schoolId);

    List<ActiveDriversResponse> getActiveDrivers(Long schoolId);

    List<NoticeResponse> getNotices(Long schoolId);

    NoticeResponse createNotice(CreateNoticeRequest request);

    List<CalendarEventResponse> getCalendarEvents(Long schoolId, int month, int year);

    ChartResponse getAttendanceReport(Long schoolId, String range);

    ChartResponse getRevenueReport(Long schoolId, String range);

    ChartResponse getStudentsGrowthReport(Long schoolId, String range);
}
