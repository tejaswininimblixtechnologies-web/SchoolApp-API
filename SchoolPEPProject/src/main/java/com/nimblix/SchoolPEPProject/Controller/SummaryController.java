package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Response.TeacherDiarySummaryResponse;
import com.nimblix.SchoolPEPProject.Service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
public class SummaryController {

    private final SummaryService summaryService;

    // Teacher Diary Summary (Month-wise)
    @GetMapping("/teacher-diary-summary")
    public ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummary(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        log.info("Getting teacher diary summary for month: {}", yearMonth);
        return summaryService.getTeacherDiarySummary(yearMonth);
    }

    // Teacher Diary Summary by Year and Month
    @GetMapping("/teacher-diary-summary/{year}/{month}")
    public ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummaryByYearMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        log.info("Getting teacher diary summary for year: {}, month: {}", year, month);
        return summaryService.getTeacherDiarySummaryByYearMonth(year, month);
    }

    // Current Month Summary
    @GetMapping("/current-month-summary")
    public ResponseEntity<TeacherDiarySummaryResponse> getCurrentMonthSummary() {
        log.info("Getting current month summary");
        return summaryService.getCurrentMonthSummary();
    }

    // Today's Summary
    @GetMapping("/today-summary")
    public ResponseEntity<TeacherDiarySummaryResponse> getTodaySummary() {
        log.info("Getting today's summary");
        return summaryService.getTodaySummary();
    }

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Summary controller is working!");
        return ResponseEntity.ok("Dashboard Summary API is working!");
    }
}
