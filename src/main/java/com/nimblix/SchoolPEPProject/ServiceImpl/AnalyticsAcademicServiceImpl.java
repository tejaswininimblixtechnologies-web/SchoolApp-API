package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.AcademicSummaryModel;
import com.nimblix.SchoolPEPProject.Model.AcademicTrendModel;
import com.nimblix.SchoolPEPProject.Repository.AnalyticsAcademicQueryRepository;
import com.nimblix.SchoolPEPProject.Service.AnalyticsAcademicService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsAcademicServiceImpl implements AnalyticsAcademicService {

    private final AnalyticsAcademicQueryRepository repository;

    public AnalyticsAcademicServiceImpl(AnalyticsAcademicQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AcademicTrendModel> getAcademicPerformanceTrend(String month) {

        List<Object[]> rows = repository.fetchAcademicPerformanceTrend(month);

        return rows.stream().map(row -> new AcademicTrendModel(
                row[0].toString(),
                row[1] == null ? 0.0 : ((Number) row[1]).doubleValue(),
                row[2] == null ? 0.0 : ((Number) row[2]).doubleValue()
        )).collect(Collectors.toList());
    }
    
    @Override
    public AcademicSummaryModel getAcademicPerformanceSummary(String month) {

        Object[] row = repository.fetchAcademicPerformanceSummary(month);

        long totalExams = row[0] == null ? 0 : ((Number) row[0]).longValue();
        double avgMarks = row[1] == null ? 0.0 : ((Number) row[1]).doubleValue();
        int highest = row[2] == null ? 0 : ((Number) row[2]).intValue();
        int lowest = row[3] == null ? 0 : ((Number) row[3]).intValue();

        // assuming marks are out of 100
        double avgPercentage = avgMarks;

        return new AcademicSummaryModel(
                totalExams,
                avgPercentage,
                highest,
                lowest
        );
    }

}
