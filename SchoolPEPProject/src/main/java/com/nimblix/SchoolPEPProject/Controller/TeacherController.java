package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Request.CalendarEventRequest;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.ClassesConductedRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.ClassesConductedResponse;
import com.nimblix.SchoolPEPProject.Response.CalendarEventResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherProfileResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherAssignedClassesResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherDiarySummaryResponse;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import com.nimblix.SchoolPEPProject.Service.SchoolService;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;
    private final AdminService adminService;
    private final SchoolService schoolService;

    // ========== TEACHER CONTEXT APIS (For Diary System) ==========

    // Get Logged-in Teacher Details (Used to auto-map diary entries to a teacher)
    @GetMapping("/profile")
    public ResponseEntity<TeacherProfileResponse> getLoggedInTeacherDetails() {
        log.info("Getting logged-in teacher details for diary context");
        TeacherProfileResponse response = teacherService.getLoggedInTeacherDetails();
        return ResponseEntity.ok(response);
    }

    // Get Teacher Assigned Classes & Subjects (For "Classes Today" section)
    @GetMapping("/assigned-classes")
    public ResponseEntity<TeacherAssignedClassesResponse> getTeacherAssignedClasses() {
        log.info("Getting teacher assigned classes & subjects for diary context");
        TeacherAssignedClassesResponse response = teacherService.getTeacherAssignedClassesAndSubjects();
        return ResponseEntity.ok(response);
    }

    // ========== DASHBOARD ENDPOINTS (Moved from DashboardController) ==========

    // Teacher Diary Summary (Month-wise)
    @GetMapping("/dashboard/teacher-diary-summary")
    public ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummary(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        log.info("Getting teacher diary summary for month: {}", yearMonth);
        return schoolService.getTeacherDiarySummary(yearMonth);
    }

    // Teacher Diary Summary by Year and Month
    @GetMapping("/dashboard/summary/{year}/{month}")
    public ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummaryByYearMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        log.info("Getting teacher diary summary for year: {}, month: {}", year, month);
        return schoolService.getTeacherDiarySummaryByYearMonth(year, month);
    }

    // Current Month Summary
    @GetMapping("/dashboard/current-month-summary")
    public ResponseEntity<TeacherDiarySummaryResponse> getCurrentMonthSummary() {
        log.info("Getting current month summary");
        return schoolService.getCurrentMonthSummary();
    }

    // Today's Summary
    @GetMapping("/dashboard/today-summary")
    public ResponseEntity<TeacherDiarySummaryResponse> getTodaySummary() {
        log.info("Getting today's summary");
        return schoolService.getTodaySummary();
    }

    // ========== CLASSES CONDUCTED ENDPOINTS (Moved from AcademicController) ==========

    // Add Class Conducted
    @PostMapping("/classes")
    public ResponseEntity<ClassesConductedResponse> createClass(@Valid @RequestBody ClassesConductedRequest request) {
        log.info("Creating class conducted for subjectId: {}", request.getSubjectId());
        return teacherService.createClass(request);
    }

    // Update Class Conducted
    @PutMapping("/classes/{classId}")
    public ResponseEntity<ClassesConductedResponse> updateClass(
            @PathVariable Long classId,
            @Valid @RequestBody ClassesConductedRequest request) {
        log.info("Updating class: {}", classId);
        return teacherService.updateClass(classId, request);
    }

    // Delete Class Conducted
    @DeleteMapping("/classes/{classId}")
    public ResponseEntity<String> deleteClass(@PathVariable Long classId) {
        log.info("Deleting class: {}", classId);
        return teacherService.deleteClass(classId);
    }

    // Get Class by ID
    @GetMapping("/classes/{classId}")
    public ResponseEntity<ClassesConductedResponse> getClassById(@PathVariable Long classId) {
        log.info("Getting class: {}", classId);
        return teacherService.getClassById(classId);
    }

    // Get All Teacher Classes
    @GetMapping("/classes")
    public ResponseEntity<java.util.List<ClassesConductedResponse>> getTeacherClasses() {
        log.info("Getting all teacher classes");
        return teacherService.getTeacherClasses();
    }

    // Get Classes by Date
    @GetMapping("/classes/date/{classDate}")
    public ResponseEntity<java.util.List<ClassesConductedResponse>> getClassesByDate(@PathVariable java.time.LocalDate classDate) {
        log.info("Getting classes for date: {}", classDate);
        return teacherService.getClassesByDate(classDate);
    }

    // Get Classes by Date Range
    @GetMapping("/classes/range")
    public ResponseEntity<java.util.List<ClassesConductedResponse>> getClassesByDateRange(
            @RequestParam java.time.LocalDate startDate,
            @RequestParam java.time.LocalDate endDate) {
        log.info("Getting classes from {} to {}", startDate, endDate);
        return teacherService.getClassesByDateRange(startDate, endDate);
    }

    // Get Today's Classes
    @GetMapping("/classes/today")
    public ResponseEntity<java.util.List<ClassesConductedResponse>> getTodayClasses() {
        log.info("Getting today's classes");
        return teacherService.getTodayClasses();
    }

    // ========== MEETINGS ENDPOINTS (Now using AdminService - Calendar Events with MEETING type) ==========

    // Add Meeting (as Calendar Event with MEETING type)
    @PostMapping("/meetings")
    public ResponseEntity<CalendarEventResponse> createMeeting(@Valid @RequestBody CalendarEventRequest request) {
        log.info("Creating meeting: {}", request.getEventTitle());
        // Set event type to MEETING for calendar events
        // Note: You might need to add a method to set event type in the request or handle it in the service
        return adminService.createEvent(request);
    }

    // Update Meeting
    @PutMapping("/meetings/{meetingId}")
    public ResponseEntity<CalendarEventResponse> updateMeeting(
            @PathVariable Long meetingId,
            @Valid @RequestBody CalendarEventRequest request) {
        log.info("Updating meeting: {}", meetingId);
        return adminService.updateEvent(meetingId, request);
    }

    // Delete Meeting
    @DeleteMapping("/meetings/{meetingId}")
    public ResponseEntity<String> deleteMeeting(@PathVariable Long meetingId) {
        log.info("Deleting meeting: {}", meetingId);
        return adminService.deleteEvent(meetingId);
    }

    // Get Meeting by ID
    @GetMapping("/meetings/{meetingId}")
    public ResponseEntity<CalendarEventResponse> getMeetingById(@PathVariable Long meetingId) {
        log.info("Getting meeting: {}", meetingId);
        return adminService.getEventById(meetingId);
    }

    // Get All Teacher Meetings (filter calendar events for MEETING type)
    @GetMapping("/meetings")
    public ResponseEntity<java.util.List<CalendarEventResponse>> getTeacherMeetings() {
        log.info("Getting all teacher meetings");
        // Note: You might need to add a method to filter events by type in the service
        return adminService.getTeacherEvents();
    }

    // Get Meetings by Date
    @GetMapping("/meetings/date/{meetingDate}")
    public ResponseEntity<java.util.List<CalendarEventResponse>> getMeetingsByDate(@PathVariable java.time.LocalDate meetingDate) {
        log.info("Getting meetings for date: {}", meetingDate);
        // Convert LocalDate to LocalDateTime for the service call
        java.time.LocalDateTime startDateTime = meetingDate.atStartOfDay();
        java.time.LocalDateTime endDateTime = meetingDate.atTime(23, 59, 59);
        return adminService.getTeacherEventsByDateRange(startDateTime, endDateTime);
    }

    // Get Meetings by Date Range
    @GetMapping("/meetings/range")
    public ResponseEntity<java.util.List<CalendarEventResponse>> getMeetingsByDateRange(
            @RequestParam java.time.LocalDate startDate,
            @RequestParam java.time.LocalDate endDate) {
        log.info("Getting meetings from {} to {}", startDate, endDate);
        // Convert LocalDate to LocalDateTime for the service call
        java.time.LocalDateTime startDateTime = startDate.atStartOfDay();
        java.time.LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return adminService.getTeacherEventsByDateRange(startDateTime, endDateTime);
    }

    // Get Today's Meetings
    @GetMapping("/meetings/today")
    public ResponseEntity<java.util.List<CalendarEventResponse>> getTodayMeetings() {
        log.info("Getting today's meetings");
        return adminService.getUpcomingEvents();
    }

    // ========== EXISTING TEACHER APIS ==========

    @PostMapping("/teacherRegister")
    public Map<String, String> registerTeacher(@RequestBody TeacherRegistrationRequest request) {
        return teacherService.registerTeacher(request);
    }

    @GetMapping("/getTeacher")
    public TeacherDetailsResponse getTeacherDetails(@RequestParam Long teacherId) {
        return teacherService.getTeacherDetails(teacherId);
    }

    @PutMapping("/updateTeacher/{teacherId}")
    public Map<String, String> updateTeacherDetails(@RequestBody TeacherRegistrationRequest request, @PathVariable Long teacherId) {
        return teacherService.updateTeacherDetails(request, teacherId);
    }

    @DeleteMapping("/deleteTeacher/{teacherId}/{schoolId}")
    public Map<String, String> deleteTeacherDetails(@PathVariable Long teacherId, @PathVariable Long schoolId) {
        return teacherService.deleteTeacherDetails(teacherId, schoolId);
    }

    @PostMapping("/createClassroom")
    public ResponseEntity<Map<String, String>> createClassroom(@RequestBody ClassroomRequest request) {
        return teacherService.createClassroom(request);
    }

    @PostMapping(value = "/createAssignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> createAssignment(
            @RequestPart("assignmentRequest") CreateAssignmentRequest request,
            @RequestPart("files") MultipartFile[] files) {
        return teacherService.createAssignment(request, files);
    }

    @PostMapping("/onboardSubject")
    public Map<String, String> onboardSubject(@RequestBody OnboardSubjectRequest request) {
        return teacherService.onboardSubject(request);
    }

    @PutMapping(value = "/updateAssignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> updateAssignment(
            @RequestPart("assignmentRequest") CreateAssignmentRequest request,
            @RequestPart("files") MultipartFile[] files) {
        return teacherService.updateAssignment(request, files);
    }

    @PutMapping("/updateOnboardSubject")
    public Map<String, String> updateOnboardSubject(@RequestBody OnboardSubjectRequest request) {
        return teacherService.updateOnboardSubject(request);
    }

    @DeleteMapping("/deleteAssignment/{assignmentId}/{subjectId}")
    public Map<String, String> deleteAssignment(@PathVariable Long assignmentId, @PathVariable Long subjectId) {
        return teacherService.deleteAssignment(assignmentId, subjectId);
    }

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Teacher controller is working!");
        return ResponseEntity.ok("Teacher API is working!");
    }
}
