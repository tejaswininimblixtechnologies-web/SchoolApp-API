package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nimblix.SchoolPEPProject.Request.CalendarEventRequest;
import com.nimblix.SchoolPEPProject.Response.CalendarEventResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarSummaryResponse;
import com.nimblix.SchoolPEPProject.Service.CalendarService;
import com.nimblix.SchoolPEPProject.Service.DiaryEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

    private final CalendarService calendarService;
    private final DiaryEntryService diaryEntryService;

    // ========== CALENDAR SUMMARY ENDPOINTS (Moved from DiaryEntryController) ==========

    // Get Monthly Calendar Summary
    @GetMapping("/summary")
    public ResponseEntity<MonthlyCalendarSummaryResponse> getMonthlyCalendarSummary(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        log.info("Getting monthly calendar summary for: {}", yearMonth);
        return diaryEntryService.getMonthlyCalendarSummary(yearMonth);
    }

    // Get Monthly Calendar Summary by Year and Month
    @GetMapping("/summary/{year}/{month}")
    public ResponseEntity<MonthlyCalendarSummaryResponse> getMonthlyCalendarSummaryByYearMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        log.info("Getting monthly calendar summary for year: {}, month: {}", year, month);
        YearMonth yearMonth = YearMonth.of(year, month);
        return diaryEntryService.getMonthlyCalendarSummary(yearMonth);
    }

    // ========== EXISTING CALENDAR ENDPOINTS ==========

    // CRUD Operations
    @PostMapping("/events")
    public ResponseEntity<CalendarEventResponse> createEvent(@Valid @RequestBody CalendarEventRequest request) {
        log.info("Creating calendar event: {}", request.getEventTitle());
        return calendarService.createEvent(request);
    }

    @GetMapping("/events")
    public ResponseEntity<List<CalendarEventResponse>> getTeacherEvents() {
        log.info("Getting all calendar events");
        return calendarService.getTeacherEvents();
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<CalendarEventResponse> getEventById(@PathVariable Long eventId) {
        log.info("Getting calendar event: {}", eventId);
        return calendarService.getEventById(eventId);
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<CalendarEventResponse> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody CalendarEventRequest request) {
        log.info("Updating calendar event: {}", eventId);
        return calendarService.updateEvent(eventId, request);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        log.info("Deleting calendar event: {}", eventId);
        return calendarService.deleteEvent(eventId);
    }

    // Date-based queries
    @GetMapping("/events/date/{eventDate}")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime eventDate) {
        log.info("Getting events for date: {}", eventDate);
        return calendarService.getTeacherEventsByDateRange(eventDate, eventDate);
    }

    @GetMapping("/events/range")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate) {
        log.info("Getting events from {} to {}", startDate, endDate);
        return calendarService.getTeacherEventsByDateRange(startDate, endDate);
    }

    @GetMapping("/events/upcoming")
    public ResponseEntity<List<CalendarEventResponse>> getUpcomingEvents() {
        log.info("Getting upcoming events");
        return calendarService.getUpcomingEvents();
    }

    // Monthly Calendar
    @GetMapping("/events/monthly")
    public ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(
            @RequestParam int year,
            @RequestParam int month) {
        log.info("Getting monthly calendar for {}-{}", year, month);
        return calendarService.getMonthlyCalendar(year, month);
    }

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Calendar controller is working!");
        return ResponseEntity.ok("Calendar API is working!");
    }
}
