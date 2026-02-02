package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.DiaryEntryRequest;
import com.nimblix.SchoolPEPProject.Response.DiaryEntryResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarSummaryResponse;
import org.springframework.http.ResponseEntity;

import java.time.YearMonth;
import java.util.List;

public interface DiaryEntryService {

    // CRUD Operations
    ResponseEntity<DiaryEntryResponse> createEntry(DiaryEntryRequest request);
    ResponseEntity<DiaryEntryResponse> updateEntry(Long entryId, DiaryEntryRequest request);
    ResponseEntity<String> deleteEntry(Long entryId);
    ResponseEntity<DiaryEntryResponse> getEntryById(Long entryId);
    ResponseEntity<List<DiaryEntryResponse>> getTeacherEntries();

    // Calendar Summary
    ResponseEntity<MonthlyCalendarSummaryResponse> getMonthlyCalendarSummary(YearMonth yearMonth);
}
