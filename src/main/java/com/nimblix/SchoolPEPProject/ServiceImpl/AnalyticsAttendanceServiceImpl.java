package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.AttendanceSummaryModel;
import com.nimblix.SchoolPEPProject.Model.AttendanceTrendModel;
import com.nimblix.SchoolPEPProject.Repository.AnalyticsAttendanceRepository;
import com.nimblix.SchoolPEPProject.Service.AnalyticsAttendanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsAttendanceServiceImpl implements AnalyticsAttendanceService {

    private final AnalyticsAttendanceRepository repository;

    public AnalyticsAttendanceServiceImpl(AnalyticsAttendanceRepository repository) {
        this.repository = repository;
    }

    // ================= TASK-1 (DO NOT TOUCH) =================
    @Override
    public List<AttendanceTrendModel> getAttendanceTrend(String month) {

        List<Object[]> rows = repository.fetchAttendanceTrend(month);

        return rows.stream().map(row -> {

            Long total = (Long) row[1];
            Long present = (Long) row[2];
            Long absent = (Long) row[3];

            double percentage = total == 0 ? 0.0 : (present * 100.0) / total;

            return new AttendanceTrendModel(
                    row[0].toString(),
                    total,
                    present,
                    absent,
                    percentage
            );
        }).toList();
    }

    // ================= TASK-2 (NEW ADDITION) =================
    @Override
    public AttendanceSummaryModel getAttendanceSummary(String month) {

        List<Object[]> rows = repository.fetchAttendanceSummary(month);

        // ✅ No data case
        if (rows == null || rows.isEmpty()) {
            return new AttendanceSummaryModel(0.0, 0, 0, 0);
        }

        Object[] result = rows.get(0);

        long present = result[0] == null ? 0 : ((Number) result[0]).longValue();
        long absent = result[1] == null ? 0 : ((Number) result[1]).longValue();
        long workingDays = result[2] == null ? 0 : ((Number) result[2]).longValue();

        long total = present + absent;

        double avgPercentage =
                total == 0 ? 0.0 :
                Math.round((present * 100.0 / total) * 100.0) / 100.0;


        return new AttendanceSummaryModel(
                avgPercentage,
                present,
                absent,
                workingDays
        );
    }


}
