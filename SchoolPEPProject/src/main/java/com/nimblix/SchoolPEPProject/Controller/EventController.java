package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.EventRequest;
import com.nimblix.SchoolPEPProject.Response.EventResponse;
import com.nimblix.SchoolPEPProject.Service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    // Add Event
    @PostMapping("/add")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        log.info("Creating event: {}", request.getEventTitle());
        return eventService.createEvent(request);
    }

    // Get Events by Date
    @GetMapping("/add/date/{eventDate}")
    public ResponseEntity<List<EventResponse>> getEventsByDate(@PathVariable LocalDate eventDate) {
        log.info("Getting events for date: {}", eventDate);
        return eventService.getEventsByDate(eventDate);
    }

    // Get Upcoming Events (7 days)
    @GetMapping("/upcoming/week")
    public ResponseEntity<List<EventResponse>> getUpcomingEventsWeek() {
        log.info("Getting upcoming events for next 7 days");
        return eventService.getUpcomingEvents(7);
    }

    // Get Upcoming Events (30 days)
    @GetMapping("/upcoming/month")
    public ResponseEntity<List<EventResponse>> getUpcomingEventsMonth() {
        log.info("Getting upcoming events for next 30 days");
        return eventService.getUpcomingEvents(30);
    }

    // Get Upcoming Events (with parameter)
    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponse>> getUpcomingEvents(@RequestParam(defaultValue = "30") Integer days) {
        log.info("Getting upcoming events for next {} days", days);
        return eventService.getUpcomingEvents(days);
    }

    // Get School-wide Events
    @GetMapping("/school-wide")
    public ResponseEntity<List<EventResponse>> getSchoolWideEvents() {
        log.info("Getting school-wide events");
        return eventService.getSchoolWideEvents();
    }

    // Get Today's Events
    @GetMapping("/today")
    public ResponseEntity<List<EventResponse>> getTodayEvents() {
        log.info("Getting today's events");
        return eventService.getTodayEvents();
    }

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Event controller is working!");
        return ResponseEntity.ok("Event API is working!");
    }
}
