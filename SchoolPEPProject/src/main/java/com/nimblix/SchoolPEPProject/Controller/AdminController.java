package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Designation;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Request.CalendarEventRequest;
import com.nimblix.SchoolPEPProject.Request.DiaryEntryRequest;
import com.nimblix.SchoolPEPProject.Request.QuickNoteRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Response.CalendarEventResponse;
import com.nimblix.SchoolPEPProject.Response.DiaryEntryResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarSummaryResponse;
import com.nimblix.SchoolPEPProject.Response.QuickNoteResponse;
import com.nimblix.SchoolPEPProject.Repository.DesignationRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final DesignationRepository designationRepository;
    private final RoleRepository roleRepository;

    @PostMapping("/adminlogin")
    public ResponseEntity<String> submitEmail(@RequestBody String email) {
        String response = adminService.submitEmail(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/adminregister")
    public ResponseEntity<Map<String, String>> createAdminAccount(@RequestBody AdminAccountCreateRequest request) {
        Map<String, String> response = new HashMap<>();

        try {
            Long adminId = adminService.createAdminAccount(request);
            response.put(SchoolConstants.MESSAGE, "Admin account created successfully. Admin ID: " + adminId);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put(SchoolConstants.MESSAGE, "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("message", "Error: Something went wrong. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/studentlist")
    public ResponseEntity<?> getStudentList(
            @RequestParam Long schoolId,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String status
    ) {
        try {
            List<Student> students = adminService.getStudentList(
                    schoolId,
                    classId,
                    section,
                    status
            );

            if (students == null || students.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put(SchoolConstants.MESSAGE, "No students found for the given filters");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            Map<String, Object> success = new HashMap<>();
            success.put(SchoolConstants.MESSAGE, "Students fetched successfully");
            success.put(SchoolConstants.DATA, students);
            return ResponseEntity.ok(success);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(SchoolConstants.MESSAGE, "Failed to fetch student list: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @GetMapping("/profile")
    public AdminProfileResponse getAdminProfile(
            @RequestParam Long adminId,
            @RequestParam Long schoolId
    ) {
        return adminService.getAdminProfile(adminId, schoolId);
    }

    @PostMapping("/create-designation")
    public ResponseEntity<Map<String, String>> createDesignation(@RequestParam String designationName) {
        Map<String, String> response = new HashMap<>();
        try {
            Designation designation = new Designation();
            designation.setDesignationName(designationName);
            designation.setStaffType(StaffType.TEACHING);
            designationRepository.save(designation);
            
            response.put("status", "success");
            response.put("message", "Designation created successfully: " + designationName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to create designation: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create-role")
    public ResponseEntity<Map<String, String>> createRole(@RequestParam String roleName) {
        Map<String, String> response = new HashMap<>();
        try {
            Role role = new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);
            
            response.put("status", "success");
            response.put("message", "Role created successfully: " + roleName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to create role: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ========== CALENDAR ENDPOINTS ==========

    // Calendar CRUD Operations
    @PostMapping("/calendar/events")
    public ResponseEntity<CalendarEventResponse> createEvent(@Valid @RequestBody CalendarEventRequest request) {
        log.info("Creating calendar event: {}", request.getEventTitle());
        return adminService.createEvent(request);
    }

    @GetMapping("/calendar/events")
    public ResponseEntity<List<CalendarEventResponse>> getTeacherEvents() {
        log.info("Getting all calendar events");
        return adminService.getTeacherEvents();
    }

    @GetMapping("/calendar/events/{eventId}")
    public ResponseEntity<CalendarEventResponse> getEventById(@PathVariable Long eventId) {
        log.info("Getting calendar event: {}", eventId);
        return adminService.getEventById(eventId);
    }

    @PutMapping("/calendar/events/{eventId}")
    public ResponseEntity<CalendarEventResponse> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody CalendarEventRequest request) {
        log.info("Updating calendar event: {}", eventId);
        return adminService.updateEvent(eventId, request);
    }

    @DeleteMapping("/calendar/events/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        log.info("Deleting calendar event: {}", eventId);
        return adminService.deleteEvent(eventId);
    }

    // Calendar Date-based queries
    @GetMapping("/calendar/events/date/{eventDate}")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime eventDate) {
        log.info("Getting events for date: {}", eventDate);
        return adminService.getTeacherEventsByDateRange(eventDate, eventDate);
    }

    @GetMapping("/calendar/events/range")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate) {
        log.info("Getting events from {} to {}", startDate, endDate);
        return adminService.getTeacherEventsByDateRange(startDate, endDate);
    }

    @GetMapping("/calendar/events/upcoming")
    public ResponseEntity<List<CalendarEventResponse>> getUpcomingEvents() {
        log.info("Getting upcoming events");
        return adminService.getUpcomingEvents();
    }

    // Monthly Calendar
    @GetMapping("/calendar/events/monthly")
    public ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(
            @RequestParam int year,
            @RequestParam int month) {
        log.info("Getting monthly calendar for {}-{}", year, month);
        return adminService.getMonthlyCalendar(year, month);
    }

    // Calendar Summary
    @GetMapping("/calendar/summary")
    public ResponseEntity<MonthlyCalendarSummaryResponse> getMonthlyCalendarSummary(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        log.info("Getting monthly calendar summary for: {}", yearMonth);
        return adminService.getMonthlyCalendarSummary(yearMonth);
    }

    @GetMapping("/calendar/summary/{year}/{month}")
    public ResponseEntity<MonthlyCalendarSummaryResponse> getMonthlyCalendarSummaryByYearMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        log.info("Getting monthly calendar summary for year: {}, month: {}", year, month);
        YearMonth yearMonth = YearMonth.of(year, month);
        return adminService.getMonthlyCalendarSummary(yearMonth);
    }

    // ========== DIARY ENTRY ENDPOINTS ==========

    // Diary CRUD Operations
    @PostMapping("/diary/entries")
    public ResponseEntity<DiaryEntryResponse> createEntry(@Valid @RequestBody DiaryEntryRequest request) {
        log.info("Creating diary entry: {}", request.getEntryTitle());
        return adminService.createEntry(request);
    }

    @PutMapping("/diary/entries/{entryId}")
    public ResponseEntity<DiaryEntryResponse> updateEntry(
            @PathVariable Long entryId,
            @Valid @RequestBody DiaryEntryRequest request) {
        log.info("Updating diary entry: {}", entryId);
        return adminService.updateEntry(entryId, request);
    }

    @DeleteMapping("/diary/entries/{entryId}")
    public ResponseEntity<String> deleteEntry(@PathVariable Long entryId) {
        log.info("Deleting diary entry: {}", entryId);
        return adminService.deleteEntry(entryId);
    }

    @GetMapping("/diary/entries/{entryId}")
    public ResponseEntity<DiaryEntryResponse> getEntryById(@PathVariable Long entryId) {
        log.info("Getting diary entry: {}", entryId);
        return adminService.getEntryById(entryId);
    }

    @GetMapping("/diary/entries")
    public ResponseEntity<List<DiaryEntryResponse>> getTeacherEntries() {
        log.info("Getting all teacher diary entries");
        return adminService.getTeacherEntries();
    }

    // ========== QUICK NOTES ENDPOINTS ==========

    // Quick Notes CRUD Operations
    @PostMapping("/diary/notes/add")
    public ResponseEntity<QuickNoteResponse> createQuickNote(@Valid @RequestBody QuickNoteRequest request) {
        log.info("Creating quick note: {}", request.getNoteText());
        return adminService.createQuickNote(request);
    }

    @PutMapping("/diary/notes/add/{noteId}")
    public ResponseEntity<QuickNoteResponse> updateQuickNote(
            @PathVariable Long noteId,
            @Valid @RequestBody QuickNoteRequest request) {
        log.info("Updating quick note: {}", noteId);
        return adminService.updateQuickNote(noteId, request);
    }

    @DeleteMapping("/diary/notes/add/{noteId}")
    public ResponseEntity<String> deleteQuickNote(@PathVariable Long noteId) {
        log.info("Deleting quick note: {}", noteId);
        return adminService.deleteQuickNote(noteId);
    }

    @GetMapping("/diary/notes/add/{noteId}")
    public ResponseEntity<QuickNoteResponse> getQuickNoteById(@PathVariable Long noteId) {
        log.info("Getting quick note: {}", noteId);
        return adminService.getQuickNoteById(noteId);
    }

    @GetMapping("/diary/notes/add")
    public ResponseEntity<List<QuickNoteResponse>> getTeacherNotes() {
        log.info("Getting all teacher quick notes");
        return adminService.getTeacherNotes();
    }

    @GetMapping("/diary/notes/add/date/{noteDate}")
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDate(@PathVariable LocalDate noteDate) {
        log.info("Getting quick notes for date: {}", noteDate);
        return adminService.getNotesByDate(noteDate);
    }

    @GetMapping("/diary/notes/add/range")
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Getting quick notes from {} to {}", startDate, endDate);
        return adminService.getNotesByDateRange(startDate, endDate);
    }

    @GetMapping("/diary/notes/upcoming")
    public ResponseEntity<List<QuickNoteResponse>> getUpcomingNotes() {
        log.info("Getting upcoming quick notes");
        return adminService.getUpcomingNotes();
    }

    @GetMapping("/diary/notes/today")
    public ResponseEntity<List<QuickNoteResponse>> getTodayNotes() {
        log.info("Getting today's quick notes");
        return adminService.getTodayNotes();
    }

}



