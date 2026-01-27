package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Response.TeacherDiarySummaryResponse;
import org.springframework.http.ResponseEntity;

import java.time.YearMonth;

public interface SummaryService {

    // Teacher Diary Summary (Month-wise)
    ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummary(YearMonth yearMonth);
    ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummaryByYearMonth(Integer year, Integer month);

    // Current Month Summary
    ResponseEntity<TeacherDiarySummaryResponse> getCurrentMonthSummary();

    // Today's Summary
    ResponseEntity<TeacherDiarySummaryResponse> getTodaySummary();
}
