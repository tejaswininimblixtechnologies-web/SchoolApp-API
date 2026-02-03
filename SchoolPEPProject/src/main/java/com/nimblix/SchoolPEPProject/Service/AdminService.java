package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Request.CalendarEventRequest;
import com.nimblix.SchoolPEPProject.Request.DiaryEntryRequest;
import com.nimblix.SchoolPEPProject.Request.QuickNoteRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Response.CalendarEventResponse;
import com.nimblix.SchoolPEPProject.Response.DiaryEntryResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarSummaryResponse;
import com.nimblix.SchoolPEPProject.Response.QuickNoteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public interface AdminService {
    String submitEmail(String email);
    Long createAdminAccount(AdminAccountCreateRequest request);
    List<Student> getStudentList(Long schoolId, Long classId, String section, String status);
    AdminProfileResponse getAdminProfile(Long adminId, Long schoolId);

    // ========== CALENDAR SERVICE METHODS ==========
    ResponseEntity<CalendarEventResponse> createEvent(CalendarEventRequest request);
    ResponseEntity<List<CalendarEventResponse>> getTeacherEvents();
    ResponseEntity<CalendarEventResponse> getEventById(Long eventId);
    ResponseEntity<CalendarEventResponse> updateEvent(Long eventId, CalendarEventRequest request);
    ResponseEntity<String> deleteEvent(Long eventId);
    ResponseEntity<List<CalendarEventResponse>> getTeacherEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    ResponseEntity<List<CalendarEventResponse>> getUpcomingEvents();
    ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(int year, int month);
    ResponseEntity<MonthlyCalendarSummaryResponse> getMonthlyCalendarSummary(YearMonth yearMonth);

    // ========== DIARY ENTRY SERVICE METHODS ==========
    ResponseEntity<DiaryEntryResponse> createEntry(DiaryEntryRequest request);
    ResponseEntity<DiaryEntryResponse> updateEntry(Long entryId, DiaryEntryRequest request);
    ResponseEntity<String> deleteEntry(Long entryId);
    ResponseEntity<DiaryEntryResponse> getEntryById(Long entryId);
    ResponseEntity<List<DiaryEntryResponse>> getTeacherEntries();

    // ========== QUICK NOTES SERVICE METHODS ==========
    ResponseEntity<QuickNoteResponse> createQuickNote(QuickNoteRequest request);
    ResponseEntity<QuickNoteResponse> updateQuickNote(Long noteId, QuickNoteRequest request);
    ResponseEntity<String> deleteQuickNote(Long noteId);
    ResponseEntity<QuickNoteResponse> getQuickNoteById(Long noteId);
    ResponseEntity<List<QuickNoteResponse>> getTeacherNotes();
    ResponseEntity<List<QuickNoteResponse>> getNotesByDate(LocalDate noteDate);
    ResponseEntity<List<QuickNoteResponse>> getNotesByDateRange(LocalDate startDate, LocalDate endDate);
    ResponseEntity<List<QuickNoteResponse>> getUpcomingNotes();
    ResponseEntity<List<QuickNoteResponse>> getTodayNotes();
}
