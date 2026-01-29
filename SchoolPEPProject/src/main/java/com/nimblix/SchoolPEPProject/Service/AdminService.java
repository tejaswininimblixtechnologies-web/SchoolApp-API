package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Request.MarkAttendanceRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Response.AttendanceReportResponse;
import com.nimblix.SchoolPEPProject.Response.AttendanceSummaryResponse;

import java.util.List;
import java.util.Map;

public interface AdminService {

    String submitEmail(String email);

    Long createAdminAccount(AdminAccountCreateRequest request);

    List<Student> getStudentList(
            Long schoolId,
            Long classId,
            String section,
            String status
    );

    AdminProfileResponse getAdminProfile(Long adminId, Long schoolId);

    void markStudentAttendance(MarkAttendanceRequest request);

    AttendanceSummaryResponse getAverageAttendance(Long schoolId, String date);

    long getTotalPresentCount(Long schoolId, String date);

    long getTotalAbsentCount(Long schoolId, String date);
    List<AttendanceReportResponse> getAttendanceTrend(
            Long schoolId,
            String fromDate,
            String toDate
    );
    Map<String, Object> getClassWiseAttendance(Long schoolId,
                                               String date,
                                               int page,
                                               int size,
                                               String sortBy,
                                               String sortDir);

}
