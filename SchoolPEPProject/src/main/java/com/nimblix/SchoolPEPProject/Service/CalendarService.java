package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.CalendarEventRequest;
import com.nimblix.SchoolPEPProject.Response.CalendarEventResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarService {

    // CRUD Operations
    ResponseEntity<CalendarEventResponse> createEvent(CalendarEventRequest request);
    ResponseEntity<CalendarEventResponse> updateEvent(Long eventId, CalendarEventRequest request);
    ResponseEntity<String> deleteEvent(Long eventId);
    ResponseEntity<CalendarEventResponse> getEventById(Long eventId);

    // Teacher Calendar Operations
    ResponseEntity<List<CalendarEventResponse>> getTeacherEvents();
    ResponseEntity<List<CalendarEventResponse>> getTeacherEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    ResponseEntity<List<CalendarEventResponse>> getUpcomingEvents();
    ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(int year, int month);

    // Event Type Operations
    ResponseEntity<List<CalendarEventResponse>> getEventsByType(String eventType);
    ResponseEntity<List<CalendarEventResponse>> getEventsByClassroom(Long classroomId);

    // Utility Operations
    ResponseEntity<String> cancelEvent(Long eventId);
    ResponseEntity<String> rescheduleEvent(Long eventId, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime);
    ResponseEntity<List<CalendarEventResponse>> getTodayEvents();
    ResponseEntity<List<CalendarEventResponse>> getWeekEvents();
}
