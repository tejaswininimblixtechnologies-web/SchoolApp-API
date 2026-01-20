package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.AcademicSummaryModel;
import com.nimblix.SchoolPEPProject.Model.AcademicTrendModel;
import java.util.List;

public interface AnalyticsAcademicService {

    List<AcademicTrendModel> getAcademicPerformanceTrend(String month);

    AcademicSummaryModel getAcademicPerformanceSummary(String month);

}
