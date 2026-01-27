package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Event;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.EventRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<EventResponse> createEvent(EventRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Creating event for teacher: {} on date: {}", teacher.getId(), request.getEventDate());

            // For now, create a simple response without database operations
            EventResponse response = EventResponse.builder()
                    .id(1L)
                    .eventTitle(request.getEventTitle())
                    .eventDate(request.getEventDate())
                    .description(request.getDescription())
                    .applicableClasses(request.getApplicableClasses())
                    .isSchoolWide(request.getIsSchoolWide())
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build();

            log.info("Event created successfully (test mode)");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error creating event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<EventResponse> updateEvent(Long eventId, EventRequest request) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> deleteEvent(Long eventId) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<EventResponse> getEventById(Long eventId) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<EventResponse>> getTeacherEvents() {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<EventResponse>> getEventsByDate(LocalDate eventDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Getting events for teacher: {} on date: {}", teacher.getId(), eventDate);

            // For now, return sample data for testing
            List<EventResponse> sampleEvents = List.of(
                EventResponse.builder()
                    .id(1L)
                    .eventTitle("Science Exhibition")
                    .eventDate(eventDate)
                    .description("Annual science exhibition where students showcase their projects and experiments.")
                    .applicableClasses("10th Grade, 11th Grade")
                    .isSchoolWide(false)
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build(),
                EventResponse.builder()
                    .id(2L)
                    .eventTitle("Parent-Teacher Meeting")
                    .eventDate(eventDate)
                    .description("Regular parent-teacher meeting to discuss student progress and upcoming activities.")
                    .applicableClasses("All Classes")
                    .isSchoolWide(true)
                    .status("ACTIVE")
                    .teacherId(null)
                    .teacherName("School Administration")
                    .build()
            );

            return ResponseEntity.ok(sampleEvents);

        } catch (Exception e) {
            log.error("Error getting events by date: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<EventResponse>> getSchoolWideEvents() {
        try {
            log.info("Getting school-wide events");

            // For now, return sample school-wide events
            List<EventResponse> schoolWideEvents = List.of(
                EventResponse.builder()
                    .id(3L)
                    .eventTitle("Annual Sports Day")
                    .eventDate(LocalDate.now().plusDays(7))
                    .description("Annual sports competition with various athletic events for all students.")
                    .applicableClasses("All Classes")
                    .isSchoolWide(true)
                    .status("ACTIVE")
                    .teacherId(null)
                    .teacherName("School Administration")
                    .build(),
                EventResponse.builder()
                    .id(4L)
                    .eventTitle("Cultural Festival")
                    .eventDate(LocalDate.now().plusDays(14))
                    .description("Annual cultural festival featuring music, dance, and drama performances.")
                    .applicableClasses("All Classes")
                    .isSchoolWide(true)
                    .status("ACTIVE")
                    .teacherId(null)
                    .teacherName("School Administration")
                    .build()
            );

            return ResponseEntity.ok(schoolWideEvents);

        } catch (Exception e) {
            log.error("Error getting school-wide events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getUpcomingEvents(Integer days) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(days);
            log.info("Getting upcoming events for teacher: {} from {} to {}", teacher.getId(), today, endDate);

            // For now, return sample upcoming events based on days parameter
            List<EventResponse> upcomingEvents = List.of(
                EventResponse.builder()
                    .id(5L)
                    .eventTitle("Mathematics Competition")
                    .eventDate(today.plusDays(3))
                    .description("Inter-school mathematics competition for advanced students.")
                    .applicableClasses("10th Grade, 11th Grade")
                    .isSchoolWide(false)
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build()
            );

            // Add more events if days >= 30
            if (days >= 30) {
                EventResponse monthlyEvent = EventResponse.builder()
                    .id(6L)
                    .eventTitle("Staff Development Workshop")
                    .eventDate(today.plusDays(25))
                    .description("Professional development workshop for teaching staff on new educational methodologies.")
                    .applicableClasses("Staff Only")
                    .isSchoolWide(false)
                    .status("ACTIVE")
                    .teacherId(null)
                    .teacherName("School Administration")
                    .build();
                upcomingEvents = new java.util.ArrayList<>(upcomingEvents);
                upcomingEvents.add(monthlyEvent);
            }

            return ResponseEntity.ok(upcomingEvents);

        } catch (Exception e) {
            log.error("Error getting upcoming events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<EventResponse>> getTodayEvents() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            log.info("Getting today's events for teacher: {}", teacher.getId(), today);

            // For now, return sample today's events
            List<EventResponse> todayEvents = List.of(
                EventResponse.builder()
                    .id(7L)
                    .eventTitle("Morning Assembly")
                    .eventDate(today)
                    .description("Regular morning assembly with announcements and student presentations.")
                    .applicableClasses("All Classes")
                    .isSchoolWide(true)
                    .status("ACTIVE")
                    .teacherId(null)
                    .teacherName("School Administration")
                    .build()
            );

            return ResponseEntity.ok(todayEvents);

        } catch (Exception e) {
            log.error("Error getting today's events: {}", e.getMessage());
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
}
