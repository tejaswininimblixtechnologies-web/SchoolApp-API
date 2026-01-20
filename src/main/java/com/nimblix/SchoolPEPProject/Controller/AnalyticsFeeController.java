package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Model.FeeSummaryModel;
import com.nimblix.SchoolPEPProject.Model.FeeTrendModel;
import com.nimblix.SchoolPEPProject.Service.AnalyticsFeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/fees")
public class AnalyticsFeeController {

    private final AnalyticsFeeService service;

    public AnalyticsFeeController(AnalyticsFeeService service) {
        this.service = service;
    }

    @GetMapping("/trend")
    public List<FeeTrendModel> getFeeTrend(
            @RequestParam Long schoolId,
            @RequestParam String month,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section
    ) {
        return service.getFeeTrend(schoolId, month, classId, section);
    }
    
    // ================= TASK-6 : FEE SUMMARY =================
    @GetMapping("/summary")
    public FeeSummaryModel getFeeSummary(
            @RequestParam Long schoolId,
            @RequestParam String month,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section
    ) {
        return service.getFeeSummary(schoolId, month, classId, section);
    }
}
