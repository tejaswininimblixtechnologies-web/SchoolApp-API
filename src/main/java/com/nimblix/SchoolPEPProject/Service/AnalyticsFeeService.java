package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.FeeSummaryModel;
import com.nimblix.SchoolPEPProject.Model.FeeTrendModel;

import java.util.List;

public interface AnalyticsFeeService {

    List<FeeTrendModel> getFeeTrend(
            Long schoolId,
            String month,
            Long classId,
            String section
    );
    
    FeeSummaryModel getFeeSummary(
            Long schoolId,
            String month,
            Long classId,
            String section
    );
}
