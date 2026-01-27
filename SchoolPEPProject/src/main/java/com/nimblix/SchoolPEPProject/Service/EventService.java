package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.EventRequest;
import com.nimblix.SchoolPEPProject.Response.EventResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    // CRUD Operations
    ResponseEntity<EventResponse> createEvent(EventRequest request);
    ResponseEntity<EventResponse> updateEvent(Long eventId, EventRequest request);
    ResponseEntity<String> deleteEvent(Long eventId);
    ResponseEntity<EventResponse> getEventById(Long eventId);

    // Teacher Events Operations
    ResponseEntity<List<EventResponse>> getTeacherEvents();
    ResponseEntity<List<EventResponse>> getEventsByDate(LocalDate eventDate);
    ResponseEntity<List<EventResponse>> getEventsByDateRange(LocalDate startDate, LocalDate endDate);

    // School-wide and Upcoming Events
    ResponseEntity<List<EventResponse>> getSchoolWideEvents();
    ResponseEntity<List<EventResponse>> getUpcomingEvents(Integer days);
    ResponseEntity<List<EventResponse>> getTodayEvents();
}
