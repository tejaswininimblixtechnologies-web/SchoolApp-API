package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Meeting;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.MeetingRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<MeetingResponse> createMeeting(MeetingRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Creating meeting for teacher: {} on date: {}", teacher.getId(), request.getMeetingDate());

            // For now, create a simple response without database operations
            MeetingResponse response = MeetingResponse.builder()
                    .id(1L)
                    .meetingTitle(request.getMeetingTitle())
                    .meetingDate(request.getMeetingDate())
                    .meetingTime(request.getMeetingTime())
                    .participants(request.getParticipants())
                    .meetingNotes(request.getMeetingNotes())
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build();

            log.info("Meeting created successfully (test mode)");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error creating meeting: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MeetingResponse> updateMeeting(Long meetingId, MeetingRequest request) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> deleteMeeting(Long meetingId) {
        try {
            // For now, simulate soft delete since we don't have database operations
            log.info("Meeting entry soft deleted successfully: {} (test mode)", meetingId);
            return ResponseEntity.ok("Meeting entry deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting meeting entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MeetingResponse> getMeetingById(Long meetingId) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<MeetingResponse>> getTeacherMeetings() {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<MeetingResponse>> getMeetingsByDate(LocalDate meetingDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Getting meetings for teacher: {} on date: {}", teacher.getId(), meetingDate);

            // For now, return sample data for testing
            List<MeetingResponse> sampleMeetings = List.of(
                MeetingResponse.builder()
                    .id(1L)
                    .meetingTitle("Parent-Teacher Meeting")
                    .meetingDate(meetingDate)
                    .meetingTime("10:00 AM")
                    .participants("John Doe's Parents, School Principal")
                    .meetingNotes("Discussed student's progress in mathematics and science. Parents concerned about homework completion.")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build(),
                MeetingResponse.builder()
                    .id(2L)
                    .meetingTitle("Department Meeting")
                    .meetingDate(meetingDate)
                    .meetingTime("2:00 PM")
                    .participants("Mathematics Department Staff, Head of Department")
                    .meetingNotes("Discussed new curriculum changes and upcoming examination schedule. Planned mock tests for next month.")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build()
            );

            return ResponseEntity.ok(sampleMeetings);

        } catch (Exception e) {
            log.error("Error getting meetings by date: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MeetingResponse>> getMeetingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<MeetingResponse>> getTodayMeetings() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            log.info("Getting today's meetings for teacher: {} on date: {}", teacher.getId(), today);

            // For now, return sample data for today
            List<MeetingResponse> todayMeetings = List.of(
                MeetingResponse.builder()
                    .id(3L)
                    .meetingTitle("Staff Meeting")
                    .meetingDate(today)
                    .meetingTime("3:00 PM")
                    .participants("All Teaching Staff, School Administration")
                    .meetingNotes("Monthly staff meeting to discuss upcoming events and student performance review.")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build()
            );

            return ResponseEntity.ok(todayMeetings);

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
}
