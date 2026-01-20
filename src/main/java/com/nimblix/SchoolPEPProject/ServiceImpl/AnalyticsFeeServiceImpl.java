package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.FeeSummaryModel;
import com.nimblix.SchoolPEPProject.Model.FeeTrendModel;
import com.nimblix.SchoolPEPProject.Repository.AnalyticsFeeRepository;
import com.nimblix.SchoolPEPProject.Service.AnalyticsFeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsFeeServiceImpl implements AnalyticsFeeService {

    private final AnalyticsFeeRepository repository;

    public AnalyticsFeeServiceImpl(AnalyticsFeeRepository repository) {
        this.repository = repository;
    }

    // ================= TASK-5 : FEE TREND =================
    @Override
    public List<FeeTrendModel> getFeeTrend(
            Long schoolId,
            String month,
            Long classId,
            String section
    ) {

        List<Object[]> rows =
                repository.fetchFeeTrend(schoolId, month, classId, section);

        return rows.stream().map(row -> {

            double total = row[1] == null ? 0.0 : ((Number) row[1]).doubleValue();
            double paid = row[2] == null ? 0.0 : ((Number) row[2]).doubleValue();
            double pending = row[3] == null ? 0.0 : ((Number) row[3]).doubleValue();

            return new FeeTrendModel(
                    row[0].toString(),
                    total,
                    paid,
                    pending
            );
        }).toList();
    }

    // ================= TASK-6 : FEE SUMMARY =================
    @Override
    public FeeSummaryModel getFeeSummary(
            Long schoolId,
            String month,
            Long classId,
            String section
    ) {

        Object[] row =
                repository.fetchFeeSummary(schoolId, month, classId, section);

        double total = row[0] == null ? 0.0 : ((Number) row[0]).doubleValue();
        double paid = row[1] == null ? 0.0 : ((Number) row[1]).doubleValue();
        double pending = row[2] == null ? 0.0 : ((Number) row[2]).doubleValue();

        return new FeeSummaryModel(total, paid, pending);
    }
}
