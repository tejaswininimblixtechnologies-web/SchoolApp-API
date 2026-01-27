package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.EventType;
import com.nimblix.SchoolPEPProject.Exception.ResourceNotFoundException;
import com.nimblix.SchoolPEPProject.Exception.UserNotFoundException;
import com.nimblix.SchoolPEPProject.Model.Calendar;
import com.nimblix.SchoolPEPProject.Model.Classroom;
import com.nimblix.SchoolPEPProject.Model.Subjects;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.CalendarRepository;
import com.nimblix.SchoolPEPProject.Repository.ClassroomRepository;
import com.nimblix.SchoolPEPProject.Repository.SubjectRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Request.CalendarEventRequest;
import com.nimblix.SchoolPEPProject.Response.CalendarEventResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarResponse;
import com.nimblix.SchoolPEPProject.Service.CalendarService;
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
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public ResponseEntity<CalendarEventResponse> createEvent(CalendarEventRequest request) {
        try {
            // Get authenticated teacher
            Teacher teacher = getAuthenticatedTeacher();

            // Validate date range
            if (request.getStartDateTime().isAfter(request.getEndDateTime())) {
                return ResponseEntity.badRequest().build();
            }

            // Check for duplicate events
            if (calendarRepository.existsByTeacherIdAndEventTitleAndStartDateTime(
                    teacher.getId(), request.getEventTitle(), request.getStartDateTime())) {
                return ResponseEntity.badRequest().build();
            }

            Calendar calendar = new Calendar();
            calendar.setEventTitle(request.getEventTitle());
            calendar.setEventDescription(request.getEventDescription());
            calendar.setStartDateTime(request.getStartDateTime());
            calendar.setEndDateTime(request.getEndDateTime());
            calendar.setEventType(request.getEventType());
            calendar.setRepeatType(request.getRepeatType());
            calendar.setLocation(request.getLocation());
            calendar.setIsAllDay(request.getIsAllDay());
            calendar.setReminderMinutes(request.getReminderMinutes());
            calendar.setColor(request.getColor());
            calendar.setTeacher(teacher);

            // Set classroom if provided
            if (request.getClassroomId() != null) {
                Classroom classroom = classroomRepository.findById(request.getClassroomId())
                        .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
                calendar.setClassroom(classroom);
            }

            // Set subject if provided
            if (request.getSubjectId() != null) {
                Subjects subject = subjectRepository.findById(request.getSubjectId())
                        .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
                calendar.setSubject(subject);
            }

            Calendar savedEvent = calendarRepository.save(calendar);
            log.info("Calendar event created successfully: {}", savedEvent.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedEvent));

        } catch (Exception e) {
            log.error("Error creating calendar event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CalendarEventResponse> updateEvent(Long eventId, CalendarEventRequest request) {
        try {
            Calendar calendar = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the event belongs to the authenticated teacher
            if (!calendar.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update event details
            calendar.setEventTitle(request.getEventTitle());
            calendar.setEventDescription(request.getEventDescription());
            calendar.setStartDateTime(request.getStartDateTime());
            calendar.setEndDateTime(request.getEndDateTime());
            calendar.setEventType(request.getEventType());
            calendar.setRepeatType(request.getRepeatType());
            calendar.setLocation(request.getLocation());
            calendar.setIsAllDay(request.getIsAllDay());
            calendar.setReminderMinutes(request.getReminderMinutes());
            calendar.setColor(request.getColor());

            // Update classroom if provided
            if (request.getClassroomId() != null) {
                Classroom classroom = classroomRepository.findById(request.getClassroomId())
                        .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
                calendar.setClassroom(classroom);
            }

            // Update subject if provided
            if (request.getSubjectId() != null) {
                Subjects subject = subjectRepository.findById(request.getSubjectId())
                        .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
                calendar.setSubject(subject);
            }

            Calendar updatedEvent = calendarRepository.save(calendar);
            log.info("Calendar event updated successfully: {}", updatedEvent.getId());

            return ResponseEntity.ok(mapToResponse(updatedEvent));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating calendar event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEvent(Long eventId) {
        try {
            Calendar calendar = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the event belongs to the authenticated teacher
            if (!calendar.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            calendarRepository.delete(calendar);
            log.info("Calendar event deleted successfully: {}", eventId);

            return ResponseEntity.ok("Event deleted successfully");

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting calendar event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CalendarEventResponse> getEventById(Long eventId) {
        try {
            Calendar calendar = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the event belongs to the authenticated teacher
            if (!calendar.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(mapToResponse(calendar));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting calendar event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CalendarEventResponse>> getTeacherEvents() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> events = calendarRepository.findActiveEventsByTeacherId(teacher.getId());
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting teacher events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CalendarEventResponse>> getTeacherEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> events = calendarRepository.findEventsByTeacherIdAndDateRange(
                    teacher.getId(), startDate, endDate);
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting events by date range: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CalendarEventResponse>> getUpcomingEvents() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> events = calendarRepository.findUpcomingEventsByTeacherId(
                    teacher.getId(), LocalDateTime.now());
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting upcoming events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(int year, int month) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime monthStart = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime monthEnd = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            List<Calendar> events = calendarRepository.findEventsByTeacherIdInMonth(
                    teacher.getId(), monthStart, monthEnd);

            List<CalendarEventResponse> eventResponses = events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            // Create daily summaries
            List<MonthlyCalendarResponse.DailyEventSummary> dailySummaries = createDailySummaries(events, yearMonth);

            MonthlyCalendarResponse response = MonthlyCalendarResponse.builder()
                    .yearMonth(yearMonth)
                    .monthName(yearMonth.getMonth().toString())
                    .year(year)
                    .totalEvents((long) events.size())
                    .monthStart(monthStart.toLocalDate())
                    .monthEnd(monthEnd.toLocalDate())
                    .events(eventResponses)
                    .dailySummaries(dailySummaries)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting monthly calendar: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CalendarEventResponse>> getEventsByType(String eventType) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            EventType type = EventType.valueOf(eventType.toUpperCase());
            List<Calendar> events = calendarRepository.findEventsByTeacherIdAndEventType(teacher.getId(), type);
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error getting events by type: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CalendarEventResponse>> getEventsByClassroom(Long classroomId) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> events = calendarRepository.findEventsByTeacherIdAndClassroomId(teacher.getId(), classroomId);
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting events by classroom: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> cancelEvent(Long eventId) {
        try {
            Calendar calendar = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the event belongs to the authenticated teacher
            if (!calendar.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            calendar.setStatus("CANCELLED");
            calendarRepository.save(calendar);
            log.info("Calendar event cancelled successfully: {}", eventId);

            return ResponseEntity.ok("Event cancelled successfully");

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error cancelling calendar event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> rescheduleEvent(Long eventId, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime) {
        try {
            Calendar calendar = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the event belongs to the authenticated teacher
            if (!calendar.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Validate date range
            if (newStartDateTime.isAfter(newEndDateTime)) {
                return ResponseEntity.badRequest().build();
            }

            calendar.setStartDateTime(newStartDateTime);
            calendar.setEndDateTime(newEndDateTime);
            calendarRepository.save(calendar);
            log.info("Calendar event rescheduled successfully: {}", eventId);

            return ResponseEntity.ok("Event rescheduled successfully");

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error rescheduling calendar event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CalendarEventResponse>> getTodayEvents() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
            
            List<Calendar> events = calendarRepository.findEventsByTeacherIdAndDateRange(
                    teacher.getId(), startOfDay, endOfDay);
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting today's events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CalendarEventResponse>> getWeekEvents() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDateTime startOfWeek = LocalDateTime.now().toLocalDate().atStartOfDay();
            LocalDateTime endOfWeek = startOfWeek.plusDays(7);
            
            List<Calendar> events = calendarRepository.findEventsByTeacherIdAndDateRange(
                    teacher.getId(), startOfWeek, endOfWeek);
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting week events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Helper methods
    private Teacher getAuthenticatedTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return teacherRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));
    }

    private CalendarEventResponse mapToResponse(Calendar calendar) {
        return CalendarEventResponse.builder()
                .id(calendar.getId())
                .eventTitle(calendar.getEventTitle())
                .eventDescription(calendar.getEventDescription())
                .startDateTime(calendar.getStartDateTime())
                .endDateTime(calendar.getEndDateTime())
                .eventType(calendar.getEventType())
                .repeatType(calendar.getRepeatType())
                .location(calendar.getLocation())
                .isAllDay(calendar.getIsAllDay())
                .reminderMinutes(calendar.getReminderMinutes())
                .color(calendar.getColor())
                .status(calendar.getStatus())
                .createdTime(calendar.getCreatedTime())
                .updatedTime(calendar.getUpdatedTime())
                .teacherId(calendar.getTeacher().getId())
                .teacherName(calendar.getTeacher().getFirstName() + " " + calendar.getTeacher().getLastName())
                .classroomId(calendar.getClassroom() != null ? calendar.getClassroom().getId() : null)
                .classroomName(calendar.getClassroom() != null ? calendar.getClassroom().getClassroomName() : null)
                .subjectId(calendar.getSubject() != null ? calendar.getSubject().getId() : null)
                .subjectName(calendar.getSubject() != null ? calendar.getSubject().getSubjectName() : null)
                .subjectCode(calendar.getSubject() != null ? calendar.getSubject().getCode() : null)
                .build();
    }

    private List<MonthlyCalendarResponse.DailyEventSummary> createDailySummaries(List<Calendar> events, YearMonth yearMonth) {
        Map<LocalDate, List<Calendar>> eventsByDate = events.stream()
                .collect(Collectors.groupingBy(event -> event.getStartDateTime().toLocalDate()));

        return yearMonth.atDay(1).datesUntil(yearMonth.plusMonths(1).atDay(1))
                .map(date -> {
                    List<Calendar> dayEvents = eventsByDate.getOrDefault(date, List.of());
                    List<String> eventTypes = dayEvents.stream()
                            .map(event -> event.getEventType().toString())
                            .distinct()
                            .collect(Collectors.toList());

                    return MonthlyCalendarResponse.DailyEventSummary.builder()
                            .date(date)
                            .eventCount((long) dayEvents.size())
                            .eventTypes(eventTypes)
                            .hasEvents(!dayEvents.isEmpty())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
