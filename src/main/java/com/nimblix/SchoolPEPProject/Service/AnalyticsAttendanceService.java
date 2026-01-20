package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.AttendanceSummaryModel;
import com.nimblix.SchoolPEPProject.Model.AttendanceTrendModel;

import java.util.List;

public interface AnalyticsAttendanceService {

    List<AttendanceTrendModel> getAttendanceTrend(String month);

    AttendanceSummaryModel getAttendanceSummary(String month);
}
