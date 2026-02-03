package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.AdminRepository;
import com.nimblix.SchoolPEPProject.Repository.CalendarRepository;
import com.nimblix.SchoolPEPProject.Repository.DesignationRepository;
import com.nimblix.SchoolPEPProject.Repository.NotesRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
//import com.nimblix.SchoolPEPProject.Repository.UserRepository;
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
import com.nimblix.SchoolPEPProject.Service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final DesignationRepository designationRepository;
    private final CalendarRepository calendarRepository;
    private final NotesRepository notesRepository;

    @Override
    public String submitEmail(String email) {

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (adminRepository.existsByEmailId(email)) {
            return "Email already registered.";
        }

        return "Email accepted. Continue to account creation.";
    }

    @Override
    public Long createAdminAccount(AdminAccountCreateRequest request) {

        // Email validation
        if (request.getEmail() == null ||
                !request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid Email Format");
        }

        if (adminRepository.existsByEmailId(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Mobile validation
        if (request.getAdminMobileNo() == null || request.getAdminMobileNo().length() != 10) {
            throw new IllegalArgumentException("Invalid mobile number (must be 10 digits)");
        }

        if (adminRepository.existsByMobile(request.getAdminMobileNo())) {
            throw new IllegalArgumentException("Mobile number already registered");
        }

        // Password validation
        if (!request.getPassword().equals(request.getReEnterPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Role role = roleRepository.findByRoleName(SchoolConstants.ADMIN_ROLE);

        Designation adminDesignation =
                designationRepository
                        .findByDesignationName(SchoolConstants.ADMIN)
                        .orElseThrow(() ->
                                new RuntimeException("Admin designation not found"));

        Admin admin = new Admin();
        admin.setFirstName(request.getAdminFirstName());
        admin.setLastName(request.getAdminLastName());
        admin.setEmailId(request.getEmail());
        admin.setMobile(request.getAdminMobileNo());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        admin.setRole(role);
        admin.setStaffType(StaffType.NON_TEACHING);
        admin.setDesignation(adminDesignation);
        admin.setStatus(SchoolConstants.STATUS);
        admin.setIsLogin(false);

        Admin savedAdmin = adminRepository.save(admin);

        return savedAdmin.getId();
    }

    @Override
    public List<Student> getStudentList(
            Long schoolId,
            Long classId,
            String section,
            String status
    ) {
        return studentRepository.findByAllFilters(
                schoolId,
                classId,
                section,
                status
        );
    }

    @Override
    public AdminProfileResponse getAdminProfile(Long adminId, Long schoolId) {

        Admin admin = adminRepository.findByIdAndSchoolId(adminId, schoolId);

        AdminProfileResponse response = new AdminProfileResponse();

        response.setAdminId(admin.getId());
        response.setUserId(admin.getId());
        response.setFirstName(admin.getFirstName());
        response.setLastName(admin.getLastName());
        response.setEmailId(admin.getEmailId());
        response.setMobile(admin.getMobile());
        response.setGender(admin.getGender());
        response.setDesignation(admin.getDesignation());
        response.setProfilePicture(admin.getProfilePicture());
        response.setSchoolId(admin.getSchoolId());

        return response;
    }

    // ========== CALENDAR SERVICE METHODS ==========

    public ResponseEntity<CalendarEventResponse> createEvent(CalendarEventRequest request) {
        try {
            // This would need authentication logic - for now using placeholder
            Calendar calendarEvent = new Calendar();
            calendarEvent.setEventTitle(request.getEventTitle());
            calendarEvent.setEventDescription(request.getEventDescription());
            calendarEvent.setStartDateTime(request.getStartDateTime());
            calendarEvent.setEndDateTime(request.getEndDateTime());
            calendarEvent.setEventType(request.getEventType());
            calendarEvent.setEventStatus("ACTIVE");
            
            Calendar savedEvent = calendarRepository.save(calendarEvent);
            log.info("Event created successfully: {}", savedEvent.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToCalendarResponse(savedEvent));
        } catch (Exception e) {
            log.error("Error creating event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<CalendarEventResponse>> getTeacherEvents() {
        try {
            List<Calendar> events = calendarRepository.findAll();
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToCalendarResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<CalendarEventResponse> getEventById(Long eventId) {
        try {
            Calendar calendarEvent = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            return ResponseEntity.ok(mapToCalendarResponse(calendarEvent));
        } catch (Exception e) {
            log.error("Error getting event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<CalendarEventResponse> updateEvent(Long eventId, CalendarEventRequest request) {
        try {
            Calendar calendarEvent = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            
            calendarEvent.setEventTitle(request.getEventTitle());
            calendarEvent.setEventDescription(request.getEventDescription());
            calendarEvent.setStartDateTime(request.getStartDateTime());
            calendarEvent.setEndDateTime(request.getEndDateTime());
            calendarEvent.setEventType(request.getEventType());
            
            Calendar updatedEvent = calendarRepository.save(calendarEvent);
            log.info("Event updated successfully: {}", updatedEvent.getId());
            
            return ResponseEntity.ok(mapToCalendarResponse(updatedEvent));
        } catch (Exception e) {
            log.error("Error updating event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<String> deleteEvent(Long eventId) {
        try {
            Calendar calendarEvent = calendarRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            
            calendarEvent.setEventStatus("DELETED");
            calendarRepository.save(calendarEvent);
            log.info("Event deleted successfully: {}", eventId);
            
            return ResponseEntity.ok("Event deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<CalendarEventResponse>> getTeacherEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<Calendar> events = calendarRepository.findEventsByTeacherIdInMonth(1L, startDate, endDate); // Placeholder teacher ID
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToCalendarResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting events by date range: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<CalendarEventResponse>> getUpcomingEvents() {
        try {
            List<Calendar> events = calendarRepository.findUpcomingEvents(LocalDateTime.now());
            List<CalendarEventResponse> responses = events.stream()
                    .map(this::mapToCalendarResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting upcoming events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(int year, int month) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            MonthlyCalendarResponse response = MonthlyCalendarResponse.builder()
                    .yearMonth(yearMonth)
                    .monthName(yearMonth.getMonth().toString())
                    .year(year)
                    .totalEvents(0L)
                    .monthStart(yearMonth.atDay(1))
                    .monthEnd(yearMonth.atEndOfMonth())
                    .events(java.util.Collections.emptyList())
                    .dailySummaries(java.util.Collections.emptyList())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting monthly calendar: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== DIARY ENTRY SERVICE METHODS ==========

    public ResponseEntity<DiaryEntryResponse> createEntry(DiaryEntryRequest request) {
        try {
            Notes note = new Notes();
            note.setNoteType("DIARY");
            note.setEntryTitle(request.getEntryTitle());
            note.setEntryContent(request.getEntryContent());
            note.setEntryDate(request.getEntryDate());
            
            Notes savedNote = notesRepository.save(note);
            log.info("Diary entry created successfully: {}", savedNote.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToDiaryResponse(savedNote));
        } catch (Exception e) {
            log.error("Error creating diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<DiaryEntryResponse> updateEntry(Long entryId, DiaryEntryRequest request) {
        try {
            Notes note = notesRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));
            
            note.setEntryTitle(request.getEntryTitle());
            note.setEntryContent(request.getEntryContent());
            note.setEntryDate(request.getEntryDate());
            
            Notes updatedNote = notesRepository.save(note);
            log.info("Diary entry updated successfully: {}", updatedNote.getId());
            
            return ResponseEntity.ok(mapToDiaryResponse(updatedNote));
        } catch (Exception e) {
            log.error("Error updating diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<String> deleteEntry(Long entryId) {
        try {
            Notes note = notesRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));
            
            note.setNoteStatus("DELETED");
            notesRepository.save(note);
            log.info("Diary entry deleted successfully: {}", entryId);
            
            return ResponseEntity.ok("Diary entry deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<DiaryEntryResponse> getEntryById(Long entryId) {
        try {
            Notes note = notesRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));
            return ResponseEntity.ok(mapToDiaryResponse(note));
        } catch (Exception e) {
            log.error("Error getting diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<DiaryEntryResponse>> getTeacherEntries() {
        try {
            List<Notes> entries = notesRepository.findByTeacherIdAndNoteType(1L, "DIARY"); // Placeholder teacher ID
            List<DiaryEntryResponse> responses = entries.stream()
                    .map(this::mapToDiaryResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting diary entries: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== QUICK NOTES SERVICE METHODS ==========

    public ResponseEntity<QuickNoteResponse> createQuickNote(QuickNoteRequest request) {
        try {
            Notes note = new Notes();
            note.setNoteType("QUICK");
            note.setDescription(request.getNoteText());
            note.setNoteDate(request.getNoteDate());
            note.setPriority(request.getPriority());
            note.setNoteStatus("ACTIVE");
            
            Notes savedNote = notesRepository.save(note);
            log.info("Quick note created successfully: {}", savedNote.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToQuickNoteResponse(savedNote));
        } catch (Exception e) {
            log.error("Error creating quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<QuickNoteResponse> updateQuickNote(Long noteId, QuickNoteRequest request) {
        try {
            Notes note = notesRepository.findById(noteId)
                    .orElseThrow(() -> new RuntimeException("Quick note not found"));
            
            note.setDescription(request.getNoteText());
            note.setNoteDate(request.getNoteDate());
            note.setPriority(request.getPriority());
            
            Notes updatedNote = notesRepository.save(note);
            log.info("Quick note updated successfully: {}", updatedNote.getId());
            
            return ResponseEntity.ok(mapToQuickNoteResponse(updatedNote));
        } catch (Exception e) {
            log.error("Error updating quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<String> deleteQuickNote(Long noteId) {
        try {
            Notes note = notesRepository.findById(noteId)
                    .orElseThrow(() -> new RuntimeException("Quick note not found"));
            
            note.setNoteStatus("DELETED");
            notesRepository.save(note);
            log.info("Quick note deleted successfully: {}", noteId);
            
            return ResponseEntity.ok("Quick note deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<QuickNoteResponse> getQuickNoteById(Long noteId) {
        try {
            Notes note = notesRepository.findById(noteId)
                    .orElseThrow(() -> new RuntimeException("Quick note not found"));
            return ResponseEntity.ok(mapToQuickNoteResponse(note));
        } catch (Exception e) {
            log.error("Error getting quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<QuickNoteResponse>> getTeacherNotes() {
        try {
            List<Notes> notes = notesRepository.findByTeacherIdAndNoteType(1L, "QUICK"); // Placeholder teacher ID
            List<QuickNoteResponse> responses = notes.stream()
                    .map(this::mapToQuickNoteResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting quick notes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<QuickNoteResponse>> getNotesByDate(LocalDate noteDate) {
        try {
            List<Notes> notes = notesRepository.findByNoteDate(noteDate);
            List<QuickNoteResponse> responses = notes.stream()
                    .filter(note -> "QUICK".equals(note.getNoteType()))
                    .map(this::mapToQuickNoteResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting notes by date: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<QuickNoteResponse>> getNotesByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            // Placeholder implementation - would need proper query
            List<Notes> notes = notesRepository.findByNoteDate(startDate);
            List<QuickNoteResponse> responses = notes.stream()
                    .filter(note -> "QUICK".equals(note.getNoteType()))
                    .map(this::mapToQuickNoteResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting notes by date range: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<QuickNoteResponse>> getUpcomingNotes() {
        try {
            List<Notes> notes = notesRepository.findByNoteDate(LocalDate.now());
            List<QuickNoteResponse> responses = notes.stream()
                    .filter(note -> "QUICK".equals(note.getNoteType()))
                    .map(this::mapToQuickNoteResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting upcoming notes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<QuickNoteResponse>> getTodayNotes() {
        try {
            List<Notes> notes = notesRepository.findByNoteDate(LocalDate.now());
            List<QuickNoteResponse> responses = notes.stream()
                    .filter(note -> "QUICK".equals(note.getNoteType()))
                    .map(this::mapToQuickNoteResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting today's notes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<MonthlyCalendarSummaryResponse> getMonthlyCalendarSummary(YearMonth yearMonth) {
        try {
            MonthlyCalendarSummaryResponse response = MonthlyCalendarSummaryResponse.builder()
                    .teacherId(1L) // Placeholder teacher ID
                    .teacherName("Admin") // Placeholder teacher name
                    .month(yearMonth)
                    .monthName(yearMonth.getMonth().toString())
                    .year(yearMonth.getYear())
                    .days(java.util.Collections.emptyList())
                    .totalDaysWithDiaryEntries(0L)
                    .totalDaysWithEvents(0L)
                    .totalDaysWithMeetings(0L)
                    .totalDaysWithClasses(0L)
                    .totalDaysWithQuickNotes(0L)
                    .totalDiaryEntries(0L)
                    .totalEvents(0L)
                    .totalMeetings(0L)
                    .totalClasses(0L)
                    .totalQuickNotes(0L)
                    .workingDays(22L) // Placeholder
                    .weekendDays(8L)   // Placeholder
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting monthly calendar summary: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== HELPER METHODS ==========

    private CalendarEventResponse mapToCalendarResponse(Calendar calendar) {
        return CalendarEventResponse.builder()
                .id(calendar.getId())
                .eventTitle(calendar.getEventTitle())
                .eventDescription(calendar.getEventDescription())
                .startDateTime(calendar.getStartDateTime())
                .endDateTime(calendar.getEndDateTime())
                .eventType(calendar.getEventType())
                .build();
    }

    private DiaryEntryResponse mapToDiaryResponse(Notes note) {
        return DiaryEntryResponse.builder()
                .id(note.getId())
                .entryTitle(note.getEntryTitle())
                .entryContent(note.getEntryContent())
                .entryDate(note.getEntryDate())
                .teacherId(note.getTeacherId())
                .build();
    }

    private QuickNoteResponse mapToQuickNoteResponse(Notes note) {
        return QuickNoteResponse.builder()
                .id(note.getId())
                .noteText(note.getDescription())
                .noteDate(note.getNoteDate())
                .priority(note.getPriority())
                .status(note.getNoteStatus())
                .teacherId(note.getTeacherId())
                .build();
    }

}

