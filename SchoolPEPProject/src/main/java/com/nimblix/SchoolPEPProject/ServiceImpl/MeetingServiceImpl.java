package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Calendar;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.CalendarRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Enum.EventType;
import com.nimblix.SchoolPEPProject.Request.MeetingRequest;
import com.nimblix.SchoolPEPProject.Response.MeetingResponse;
import com.nimblix.SchoolPEPProject.Service.MeetingService;
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
public class MeetingServiceImpl implements MeetingService {

    private final CalendarRepository calendarRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<MeetingResponse> createMeeting(MeetingRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Creating meeting for teacher: {} on date: {}", teacher.getId(), request.getMeetingDate());

            Calendar calendarEvent = new Calendar();
            calendarEvent.setEventType(EventType.MEETING);
            calendarEvent.setMeetingTitle(request.getMeetingTitle());
            calendarEvent.setMeetingDate(request.getMeetingDate());
            calendarEvent.setMeetingTime(request.getMeetingTime());
            calendarEvent.setParticipants(request.getParticipants());
            calendarEvent.setMeetingNotes(request.getMeetingNotes());
            calendarEvent.setMeetingStatus("ACTIVE");
            calendarEvent.setTeacher(teacher);
            
            // Set main event fields for consistency
            calendarEvent.setEventTitle(request.getMeetingTitle());
            calendarEvent.setEventDescription(request.getMeetingNotes());
            calendarEvent.setEventDate(request.getMeetingDate());
            
            // Set date time from meeting date and time
            if (request.getMeetingDate() != null && request.getMeetingTime() != null) {
                // Parse meeting time and combine with date
                LocalDateTime meetingDateTime = LocalDateTime.of(request.getMeetingDate(), 
                    java.time.LocalTime.parse(request.getMeetingTime()));
                calendarEvent.setStartDateTime(meetingDateTime);
                calendarEvent.setEndDateTime(meetingDateTime.plusHours(1)); // Default 1 hour meeting
            }

            Calendar savedEvent = calendarRepository.save(calendarEvent);
            log.info("Meeting created successfully: {}", savedEvent.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedEvent));

        } catch (Exception e) {
            log.error("Error creating meeting: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MeetingResponse> updateMeeting(Long meetingId, MeetingRequest request) {
        try {
            Calendar calendarEvent = calendarRepository.findById(meetingId)
                    .orElseThrow(() -> new RuntimeException("Meeting not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the meeting belongs to the authenticated teacher
            if (!calendarEvent.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update meeting fields
            calendarEvent.setMeetingTitle(request.getMeetingTitle());
            calendarEvent.setMeetingDate(request.getMeetingDate());
            calendarEvent.setMeetingTime(request.getMeetingTime());
            calendarEvent.setParticipants(request.getParticipants());
            calendarEvent.setMeetingNotes(request.getMeetingNotes());
            
            // Update main event fields for consistency
            calendarEvent.setEventTitle(request.getMeetingTitle());
            calendarEvent.setEventDescription(request.getMeetingNotes());
            calendarEvent.setEventDate(request.getMeetingDate());
            
            // Update date time from meeting date and time
            if (request.getMeetingDate() != null && request.getMeetingTime() != null) {
                LocalDateTime meetingDateTime = LocalDateTime.of(request.getMeetingDate(), 
                    java.time.LocalTime.parse(request.getMeetingTime()));
                calendarEvent.setStartDateTime(meetingDateTime);
                calendarEvent.setEndDateTime(meetingDateTime.plusHours(1));
            }

            Calendar updatedEvent = calendarRepository.save(calendarEvent);
            log.info("Meeting updated successfully: {}", updatedEvent.getId());

            return ResponseEntity.ok(mapToResponse(updatedEvent));

        } catch (Exception e) {
            log.error("Error updating meeting: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteMeeting(Long meetingId) {
        try {
            Calendar calendarEvent = calendarRepository.findById(meetingId)
                    .orElseThrow(() -> new RuntimeException("Meeting not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the meeting belongs to the authenticated teacher
            if (!calendarEvent.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Soft delete - change status to DELETED
            calendarEvent.setMeetingStatus("DELETED");
            calendarEvent.setEventStatus("DELETED");
            calendarEvent.setUpdatedBy(teacher.getId().toString());
            
            calendarRepository.save(calendarEvent);
            log.info("Meeting soft deleted successfully: {}", meetingId);

            return ResponseEntity.ok("Meeting deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting meeting: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MeetingResponse> getMeetingById(Long meetingId) {
        try {
            Calendar calendarEvent = calendarRepository.findById(meetingId)
                    .orElseThrow(() -> new RuntimeException("Meeting not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the meeting belongs to the authenticated teacher
            if (!calendarEvent.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(mapToResponse(calendarEvent));

        } catch (Exception e) {
            log.error("Error getting meeting: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MeetingResponse>> getTeacherMeetings() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> meetings = calendarRepository.findByTeacherIdAndEventType(teacher.getId(), EventType.MEETING);
            List<MeetingResponse> responses = meetings.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting teacher meetings: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MeetingResponse>> getMeetingsByDate(LocalDate meetingDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> meetings = calendarRepository.findByTeacherIdAndEventTypeAndEventDate(teacher.getId(), EventType.MEETING, meetingDate);
            List<MeetingResponse> responses = meetings.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting meetings by date: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MeetingResponse>> getMeetingsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Calendar> meetings = calendarRepository.findByTeacherIdAndEventTypeAndEventDateBetween(teacher.getId(), EventType.MEETING, startDate, endDate);
            List<MeetingResponse> responses = meetings.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting meetings by date range: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MeetingResponse>> getTodayMeetings() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            List<Calendar> meetings = calendarRepository.findByTeacherIdAndEventTypeAndEventDate(teacher.getId(), EventType.MEETING, today);
            List<MeetingResponse> responses = meetings.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting today's meetings: {}", e.getMessage());
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

    private MeetingResponse mapToResponse(Calendar calendarEvent) {
        try {
            return MeetingResponse.builder()
                    .id(calendarEvent.getId())
                    .meetingTitle(calendarEvent.getMeetingTitle() != null ? calendarEvent.getMeetingTitle() : calendarEvent.getEventTitle())
                    .meetingDate(calendarEvent.getMeetingDate() != null ? calendarEvent.getMeetingDate() : calendarEvent.getEventDate())
                    .meetingTime(calendarEvent.getMeetingTime())
                    .participants(calendarEvent.getParticipants())
                    .meetingNotes(calendarEvent.getMeetingNotes() != null ? calendarEvent.getMeetingNotes() : calendarEvent.getEventDescription())
                    .status(calendarEvent.getMeetingStatus() != null ? calendarEvent.getMeetingStatus() : calendarEvent.getEventStatus())
                    .teacherId(calendarEvent.getTeacher().getId())
                    .teacherName(calendarEvent.getTeacher() != null ? 
                            calendarEvent.getTeacher().getFirstName() + " " + calendarEvent.getTeacher().getLastName() : null)
                    .createdTime(calendarEvent.getCreatedTime())
                    .updatedTime(calendarEvent.getUpdatedTime())
                    .updatedBy(calendarEvent.getUpdatedBy() != null ? Long.parseLong(calendarEvent.getUpdatedBy()) : null)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping calendar event to meeting response: {}", e.getMessage());
            // Return basic response if mapping fails
            return MeetingResponse.builder()
                    .id(calendarEvent.getId())
                    .meetingTitle(calendarEvent.getMeetingTitle() != null ? calendarEvent.getMeetingTitle() : calendarEvent.getEventTitle())
                    .meetingDate(calendarEvent.getMeetingDate() != null ? calendarEvent.getMeetingDate() : calendarEvent.getEventDate())
                    .meetingTime(calendarEvent.getMeetingTime())
                    .participants(calendarEvent.getParticipants())
                    .meetingNotes(calendarEvent.getMeetingNotes() != null ? calendarEvent.getMeetingNotes() : calendarEvent.getEventDescription())
                    .teacherId(calendarEvent.getTeacher().getId())
                    .teacherName(calendarEvent.getTeacher() != null ? 
                            calendarEvent.getTeacher().getFirstName() + " " + calendarEvent.getTeacher().getLastName() : null)
                    .build();
        }
    }
}
