package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.MeetingRequest;
import com.nimblix.SchoolPEPProject.Response.MeetingResponse;
import com.nimblix.SchoolPEPProject.Service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;

    // Add Meeting Entry
    @PostMapping("/entry")
    public ResponseEntity<MeetingResponse> createMeeting(@Valid @RequestBody MeetingRequest request) {
        log.info("Creating meeting: {}", request.getMeetingTitle());
        return meetingService.createMeeting(request);
    }

    // Update Meeting Entry
    @PutMapping("/entry/{meetingId}")
    public ResponseEntity<MeetingResponse> updateMeeting(
            @PathVariable Long meetingId,
            @Valid @RequestBody MeetingRequest request) {
        log.info("Updating meeting: {}", meetingId);
        return meetingService.updateMeeting(meetingId, request);
    }

    // Delete Meeting Entry
    @DeleteMapping("/entry/{meetingId}")
    public ResponseEntity<String> deleteMeeting(@PathVariable Long meetingId) {
        log.info("Deleting meeting: {}", meetingId);
        return meetingService.deleteMeeting(meetingId);
    }

    // Get Meeting Entry by ID
    @GetMapping("/entry/{meetingId}")
    public ResponseEntity<MeetingResponse> getMeetingById(@PathVariable Long meetingId) {
        log.info("Getting meeting: {}", meetingId);
        return meetingService.getMeetingById(meetingId);
    }

    // Get All Teacher Meetings
    @GetMapping("/entry")
    public ResponseEntity<List<MeetingResponse>> getTeacherMeetings() {
        log.info("Getting all teacher meetings");
        return meetingService.getTeacherMeetings();
    }

    // Get Meetings by Date
    @GetMapping("/entry/date/{meetingDate}")
    public ResponseEntity<List<MeetingResponse>> getMeetingsByDate(@PathVariable LocalDate meetingDate) {
        log.info("Getting meetings for date: {}", meetingDate);
        return meetingService.getMeetingsByDate(meetingDate);
    }

    // Get Meetings by Date Range
    @GetMapping("/entry/range")
    public ResponseEntity<List<MeetingResponse>> getMeetingsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Getting meetings from {} to {}", startDate, endDate);
        return meetingService.getMeetingsByDateRange(startDate, endDate);
    }

    // Get Today's Meetings
    @GetMapping("/entry/today")
    public ResponseEntity<List<MeetingResponse>> getTodayMeetings() {
        log.info("Getting today's meetings");
        return meetingService.getTodayMeetings();
    }

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Meeting controller is working!");
        return ResponseEntity.ok("Meeting API is working!");
    }
}
