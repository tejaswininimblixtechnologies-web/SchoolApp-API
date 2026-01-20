package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Model.AcademicSummaryModel;
import com.nimblix.SchoolPEPProject.Model.AcademicTrendModel;
import com.nimblix.SchoolPEPProject.Service.AnalyticsAcademicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsAcademicController {

    private final AnalyticsAcademicService service;

    public AnalyticsAcademicController(AnalyticsAcademicService service) {
        this.service = service;
    }

    // ================= TASK 3 : ACADEMIC PERFORMANCE TREND =================
    @GetMapping("/academic-performance/trend")
    public List<AcademicTrendModel> getAcademicPerformanceTrend(
            @RequestParam String month
    ) {
        return service.getAcademicPerformanceTrend(month);
    }
    
    @GetMapping("/academic-performance/summary")
    public AcademicSummaryModel getAcademicSummary(
            @RequestParam String month
    ) {
        return service.getAcademicPerformanceSummary(month);
    }

}
