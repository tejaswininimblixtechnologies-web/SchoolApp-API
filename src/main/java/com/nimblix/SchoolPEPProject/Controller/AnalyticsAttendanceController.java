package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Model.AttendanceSummaryModel;
import com.nimblix.SchoolPEPProject.Model.AttendanceTrendModel;
import com.nimblix.SchoolPEPProject.Service.AnalyticsAttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/attendance")
public class AnalyticsAttendanceController {

    private final AnalyticsAttendanceService service;

    public AnalyticsAttendanceController(AnalyticsAttendanceService service) {
        this.service = service;
    }

    @GetMapping("/trend")
    public List<AttendanceTrendModel> getAttendanceTrend(
            @RequestParam String month
    ) {
        return service.getAttendanceTrend(month);
    }

    @GetMapping("/summary")
    public ResponseEntity<AttendanceSummaryModel> getAttendanceSummary(
            @RequestParam String month
    ) {
        return ResponseEntity.ok(
                service.getAttendanceSummary(month)
        );
    }
}
