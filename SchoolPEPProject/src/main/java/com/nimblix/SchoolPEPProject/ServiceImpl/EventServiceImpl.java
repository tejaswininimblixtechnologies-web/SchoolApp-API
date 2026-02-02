package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Calendar;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.CalendarRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Request.EventRequest;
import com.nimblix.SchoolPEPProject.Response.EventResponse;
import com.nimblix.SchoolPEPProject.Service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final CalendarRepository calendarRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<EventResponse> createEvent(EventRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Creating event for teacher: {} on date: {}", teacher.getId(), request.getEventDate());

            Calendar calendarEvent = new Calendar();
            calendarEvent.setEventType(com.nimblix.SchoolPEPProject.Enum.EventType.SCHOOL_EVENT);
            calendarEvent.setEventTitle(request.getEventTitle());
            calendarEvent.setEventDescription(request.getDescription());
            calendarEvent.setEventDate(request.getEventDate());
            calendarEvent.setApplicableClasses(request.getApplicableClasses());
            calendarEvent.setSchoolWide(request.getIsSchoolWide());
            calendarEvent.setEventStatus("ACTIVE");
            calendarEvent.setTeacher(teacher);
            
            // Set date time from event date
            if (request.getEventDate() != null) {
                LocalDateTime eventDateTime = LocalDateTime.of(request.getEventDate(), java.time.LocalTime.of(9, 0));
                calendarEvent.setStartDateTime(eventDateTime);
                calendarEvent.setEndDateTime(eventDateTime.plusHours(1));
            }

            Calendar savedEvent = calendarRepository.save(calendarEvent);
            log.info("Event created successfully: {}", savedEvent.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedEvent));

        } catch (Exception e) {
            log.error("Error creating event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<EventResponse> updateEvent(Long eventId, EventRequest request) {
        try {
            Calendar calendarEvent = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the event belongs to the authenticated teacher
            if (!calendarEvent.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update event fields
            calendarEvent.setEventTitle(request.getEventTitle());
            calendarEvent.setEventDescription(request.getDescription());
            calendarEvent.setEventDate(request.getEventDate());
            calendarEvent.setApplicableClasses(request.getApplicableClasses());
            calendarEvent.setSchoolWide(request.getIsSchoolWide());
            
            // Update date time from event date
            if (request.getEventDate() != null) {
                LocalDateTime eventDateTime = LocalDateTime.of(request.getEventDate(), java.time.LocalTime.of(9, 0));
                calendarEvent.setStartDateTime(eventDateTime);
                calendarEvent.setEndDateTime(eventDateTime.plusHours(1));
            }

            Calendar updatedEvent = calendarRepository.save(calendarEvent);
            log.info("Event updated successfully: {}", updatedEvent.getId());

            return ResponseEntity.ok(mapToResponse(updatedEvent));

        } catch (Exception e) {
            log.error("Error updating event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEvent(Long eventId) {
        try {
            Calendar calendarEvent = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the event belongs to the authenticated teacher
            if (!calendarEvent.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Soft delete - change status to DELETED
            calendarEvent.setEventStatus("DELETED");
            calendarEvent.setUpdatedBy(teacher.getId().toString());
            
            calendarRepository.save(calendarEvent);
            log.info("Event soft deleted successfully: {}", eventId);

            return ResponseEntity.ok("Event deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<EventResponse> getEventById(Long eventId) {
        try {
            Calendar calendarEvent = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the event belongs to the authenticated teacher
            if (!calendarEvent.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(mapToResponse(calendarEvent));

        } catch (Exception e) {
            log.error("Error getting event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getTeacherEvents() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> events = calendarRepository.findByTeacherIdAndEventType(teacher.getId(), com.nimblix.SchoolPEPProject.Enum.EventType.SCHOOL_EVENT);
            List<EventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting teacher events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getEventsByDate(LocalDate eventDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> events = calendarRepository.findByTeacherIdAndEventTypeAndEventDate(teacher.getId(), com.nimblix.SchoolPEPProject.Enum.EventType.SCHOOL_EVENT, eventDate);
            List<EventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting events by date: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> events = calendarRepository.findByTeacherIdAndEventTypeAndEventDateBetween(teacher.getId(), com.nimblix.SchoolPEPProject.Enum.EventType.SCHOOL_EVENT, startDate, endDate);
            List<EventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting events by date range: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getSchoolWideEvents() {
        try {
            List<Calendar> events = calendarRepository.findSchoolWideEvents();
            List<EventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting school-wide events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getTodayEvents() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            List<Calendar> events = calendarRepository.findByTeacherIdAndEventTypeAndEventDate(teacher.getId(), com.nimblix.SchoolPEPProject.Enum.EventType.SCHOOL_EVENT, today);
            List<EventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting today's events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getUpcomingEvents(Integer days) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(days);
            List<Calendar> events = calendarRepository.findByTeacherIdAndEventTypeAndEventDateBetween(teacher.getId(), com.nimblix.SchoolPEPProject.Enum.EventType.SCHOOL_EVENT, today, endDate);
            List<EventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting upcoming events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Helper method
    private Teacher getAuthenticatedTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return teacherRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    private EventResponse mapToResponse(Calendar calendarEvent) {
        try {
            return EventResponse.builder()
                    .id(calendarEvent.getId())
                    .eventTitle(calendarEvent.getEventTitle())
                    .eventDate(calendarEvent.getEventDate())
                    .description(calendarEvent.getEventDescription())
                    .applicableClasses(calendarEvent.getApplicableClasses())
                    .isSchoolWide(calendarEvent.getSchoolWide())
                    .status(calendarEvent.getEventStatus())
                    .teacherId(calendarEvent.getTeacher().getId())
                    .teacherName(calendarEvent.getTeacher() != null ? 
                            calendarEvent.getTeacher().getFirstName() + " " + calendarEvent.getTeacher().getLastName() : null)
                    .createdTime(calendarEvent.getCreatedTime())
                    .updatedTime(calendarEvent.getUpdatedTime())
                    .updatedBy(calendarEvent.getUpdatedBy() != null ? Long.parseLong(calendarEvent.getUpdatedBy()) : null)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping calendar event to event response: {}", e.getMessage());
            // Return basic response if mapping fails
            return EventResponse.builder()
                    .id(calendarEvent.getId())
                    .eventTitle(calendarEvent.getEventTitle())
                    .eventDate(calendarEvent.getEventDate())
                    .description(calendarEvent.getEventDescription())
                    .applicableClasses(calendarEvent.getApplicableClasses())
                    .isSchoolWide(calendarEvent.getSchoolWide())
                    .teacherId(calendarEvent.getTeacher().getId())
                    .teacherName(calendarEvent.getTeacher() != null ? 
                            calendarEvent.getTeacher().getFirstName() + " " + calendarEvent.getTeacher().getLastName() : null)
                    .build();
        }
    }
}
