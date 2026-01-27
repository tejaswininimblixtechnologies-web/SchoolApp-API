package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nimblix.SchoolPEPProject.Request.CalendarEventRequest;
import com.nimblix.SchoolPEPProject.Response.CalendarEventResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarResponse;
import com.nimblix.SchoolPEPProject.Service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

    private final CalendarService calendarService;

    // CRUD Operations
    @PostMapping("/events")
    public ResponseEntity<CalendarEventResponse> createEvent(@Valid @RequestBody CalendarEventRequest request) {
        log.info("Creating calendar event: {}", request.getEventTitle());
        return calendarService.createEvent(request);
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

    @GetMapping("/events/{eventId}")
    public ResponseEntity<CalendarEventResponse> getEventById(@PathVariable Long eventId) {
        log.info("Getting calendar event: {}", eventId);
        return calendarService.getEventById(eventId);
    }

    // Teacher Calendar Operations
    @GetMapping("/events")
    public ResponseEntity<List<CalendarEventResponse>> getTeacherEvents() {
        log.info("Getting all teacher events");
        return calendarService.getTeacherEvents();
    }

    @GetMapping("/events/range")
    public ResponseEntity<List<CalendarEventResponse>> getTeacherEventsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        log.info("Getting events from {} to {}", startDate, endDate);
        return calendarService.getTeacherEventsByDateRange(startDate, endDate);
    }

    @GetMapping("/events/upcoming")
    public ResponseEntity<List<CalendarEventResponse>> getUpcomingEvents() {
        log.info("Getting upcoming events");
        return calendarService.getUpcomingEvents();
    }

    @GetMapping("/events/monthly")
    public ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(
            @RequestParam int year,
            @RequestParam int month) {
        log.info("Getting monthly calendar for {}-{}", year, month);
        return calendarService.getMonthlyCalendar(year, month);
    }

    // Event Type Operations
    @GetMapping("/events/type/{eventType}")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByType(@PathVariable String eventType) {
        log.info("Getting events by type: {}", eventType);
        return calendarService.getEventsByType(eventType);
    }

    @GetMapping("/events/classroom/{classroomId}")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByClassroom(@PathVariable Long classroomId) {
        log.info("Getting events by classroom: {}", classroomId);
        return calendarService.getEventsByClassroom(classroomId);
    }

    // Utility Operations
    @PostMapping("/events/{eventId}/cancel")
    public ResponseEntity<String> cancelEvent(@PathVariable Long eventId) {
        log.info("Cancelling calendar event: {}", eventId);
        return calendarService.cancelEvent(eventId);
    }

    @PostMapping("/events/{eventId}/reschedule")
    public ResponseEntity<String> rescheduleEvent(
            @PathVariable Long eventId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime newStartDateTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime newEndDateTime) {
        log.info("Rescheduling calendar event: {} to {} - {}", eventId, newStartDateTime, newEndDateTime);
        return calendarService.rescheduleEvent(eventId, newStartDateTime, newEndDateTime);
    }

    @GetMapping("/events/today")
    public ResponseEntity<List<CalendarEventResponse>> getTodayEvents() {
        log.info("Getting today's events");
        return calendarService.getTodayEvents();
    }

    @GetMapping("/events/week")
    public ResponseEntity<List<CalendarEventResponse>> getWeekEvents() {
        log.info("Getting week events");
        return calendarService.getWeekEvents();
    }

    // Quick Event Creation Endpoints
    @PostMapping("/events/quick/class")
    public ResponseEntity<CalendarEventResponse> createClassEvent(
            @RequestParam String title,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDateTime,
            @RequestParam Long classroomId,
            @RequestParam(required = false) Long subjectId) {
        
        CalendarEventRequest request = new CalendarEventRequest();
        request.setEventTitle(title);
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setClassroomId(classroomId);
        request.setSubjectId(subjectId);
        request.setEventType(com.nimblix.SchoolPEPProject.Enum.EventType.CLASS_SCHEDULE);
        
        return calendarService.createEvent(request);
    }

    @PostMapping("/events/quick/exam")
    public ResponseEntity<CalendarEventResponse> createExamEvent(
            @RequestParam String title,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDateTime,
            @RequestParam Long classroomId,
            @RequestParam(required = false) Long subjectId) {
        
        CalendarEventRequest request = new CalendarEventRequest();
        request.setEventTitle(title);
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setClassroomId(classroomId);
        request.setSubjectId(subjectId);
        request.setEventType(com.nimblix.SchoolPEPProject.Enum.EventType.EXAM);
        
        return calendarService.createEvent(request);
    }

    @PostMapping("/events/quick/meeting")
    public ResponseEntity<CalendarEventResponse> createMeetingEvent(
            @RequestParam String title,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDateTime,
            @RequestParam(required = false) String location) {
        
        CalendarEventRequest request = new CalendarEventRequest();
        request.setEventTitle(title);
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setLocation(location);
        request.setEventType(com.nimblix.SchoolPEPProject.Enum.EventType.MEETING);
        
        return calendarService.createEvent(request);
    }
}
