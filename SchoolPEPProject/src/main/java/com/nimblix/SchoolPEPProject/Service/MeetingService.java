package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.MeetingRequest;
import com.nimblix.SchoolPEPProject.Response.MeetingResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface MeetingService {

    // CRUD Operations
    ResponseEntity<MeetingResponse> createMeeting(MeetingRequest request);
    ResponseEntity<MeetingResponse> updateMeeting(Long meetingId, MeetingRequest request);
    ResponseEntity<String> deleteMeeting(Long meetingId);
    ResponseEntity<MeetingResponse> getMeetingById(Long meetingId);

    // Teacher Meetings Operations
    ResponseEntity<List<MeetingResponse>> getTeacherMeetings();
    ResponseEntity<List<MeetingResponse>> getMeetingsByDate(LocalDate meetingDate);
    ResponseEntity<List<MeetingResponse>> getMeetingsByDateRange(LocalDate startDate, LocalDate endDate);
    ResponseEntity<List<MeetingResponse>> getTodayMeetings();
}
