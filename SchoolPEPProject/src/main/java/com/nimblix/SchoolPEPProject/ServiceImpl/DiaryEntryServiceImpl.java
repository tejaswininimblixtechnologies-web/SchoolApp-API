package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Notes;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.NotesRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Request.DiaryEntryRequest;
import com.nimblix.SchoolPEPProject.Response.DiaryEntryResponse;
import com.nimblix.SchoolPEPProject.Response.CalendarDaySummaryResponse;
import com.nimblix.SchoolPEPProject.Response.MonthlyCalendarSummaryResponse;
import com.nimblix.SchoolPEPProject.Service.DiaryEntryService;
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
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiaryEntryServiceImpl implements DiaryEntryService {

    private final NotesRepository notesRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<DiaryEntryResponse> createEntry(DiaryEntryRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();

            Notes note = new Notes();
            note.setNoteType("DIARY");
            note.setEntryTitle(request.getEntryTitle());
            note.setEntryContent(request.getEntryContent());
            note.setEntryDate(request.getEntryDate());
            note.setTeacher(teacher);
            
            // Set main fields for consistency
            note.setTitle(request.getEntryTitle());
            note.setDescription(request.getEntryContent());
            note.setNoteDate(request.getEntryDate().toLocalDate());

            Notes savedNote = notesRepository.save(note);
            log.info("Diary entry created successfully: {}", savedNote.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedNote));

        } catch (Exception e) {
            log.error("Error creating diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<DiaryEntryResponse> updateEntry(Long entryId, DiaryEntryRequest request) {
        try {
            Notes note = notesRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the entry belongs to the authenticated teacher
            if (!note.getTeacherId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update diary summary
            note.setEntryTitle(request.getEntryTitle());
            note.setEntryContent(request.getEntryContent());
            note.setEntryDate(request.getEntryDate());
            
            // Update main fields for consistency
            note.setTitle(request.getEntryTitle());
            note.setDescription(request.getEntryContent());
            note.setNoteDate(request.getEntryDate().toLocalDate());
            
            // Track who updated the entry
            note.setUpdatedBy(teacher.getId().toString());

            Notes updatedNote = notesRepository.save(note);
            log.info("Diary entry updated successfully: {}", updatedNote.getId());

            return ResponseEntity.ok(mapToResponse(updatedNote));

        } catch (Exception e) {
            log.error("Error updating diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEntry(Long entryId) {
        try {
            Notes note = notesRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the entry belongs to the authenticated teacher
            if (!note.getTeacherId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Soft delete - change status to DELETED instead of removing from database
            note.setEntryStatus("DELETED");
            note.setNoteStatus("DELETED");
            note.setUpdatedBy(teacher.getId().toString());
            
            notesRepository.save(note);
            log.info("Diary entry soft deleted successfully: {}", entryId);

            return ResponseEntity.ok("Diary entry deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<DiaryEntryResponse> getEntryById(Long entryId) {
        try {
            Notes note = notesRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the entry belongs to the authenticated teacher
            if (!note.getTeacherId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(mapToResponse(note));

        } catch (Exception e) {
            log.error("Error getting diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<DiaryEntryResponse>> getTeacherEntries() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Notes> entries = notesRepository.findByTeacherIdAndNoteType(teacher.getId(), "DIARY");
            List<DiaryEntryResponse> responses = entries.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting teacher diary entries: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MonthlyCalendarSummaryResponse> getMonthlyCalendarSummary(YearMonth yearMonth) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Getting monthly calendar summary for teacher: {} for month: {}", teacher.getId(), yearMonth);

            LocalDate monthStart = yearMonth.atDay(1);
            LocalDate monthEnd = yearMonth.atEndOfMonth();
            LocalDate today = LocalDate.now();

            List<CalendarDaySummaryResponse> days = new ArrayList<>();
            Random random = new Random();

            // Generate sample data for each day in the month
            for (LocalDate date = monthStart; !date.isAfter(monthEnd); date = date.plusDays(1)) {
                CalendarDaySummaryResponse daySummary = CalendarDaySummaryResponse.builder()
                        .date(date.toString())
                        .dayOfWeek(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                        .hasDiaryEntry(random.nextBoolean())
                        .hasEvents(random.nextBoolean())
                        .hasMeetings(random.nextBoolean())
                        .hasClasses(random.nextBoolean())
                        .hasQuickNotes(random.nextBoolean())
                        .diaryEntryCount(random.nextBoolean() ? random.nextInt(3) + 1 : 0L)
                        .eventCount(random.nextBoolean() ? random.nextInt(2) + 1 : 0L)
                        .meetingCount(random.nextBoolean() ? random.nextInt(2) + 1 : 0L)
                        .classCount(random.nextBoolean() ? random.nextInt(5) + 1 : 0L)
                        .quickNoteCount(random.nextBoolean() ? random.nextInt(3) + 1 : 0L)
                        .dayType(date.getDayOfWeek().getValue() <= 5 ? "WEEKDAY" : "WEEKEND")
                        .isToday(date.equals(today))
                        .build();

                days.add(daySummary);
            }

            // Calculate summary statistics
            long totalDaysWithDiaryEntries = days.stream().mapToLong(d -> d.getHasDiaryEntry() ? 1 : 0).sum();
            long totalDaysWithEvents = days.stream().mapToLong(d -> d.getHasEvents() ? 1 : 0).sum();
            long totalDaysWithMeetings = days.stream().mapToLong(d -> d.getHasMeetings() ? 1 : 0).sum();
            long totalDaysWithClasses = days.stream().mapToLong(d -> d.getHasClasses() ? 1 : 0).sum();
            long totalDaysWithQuickNotes = days.stream().mapToLong(d -> d.getHasQuickNotes() ? 1 : 0).sum();

            long totalDiaryEntries = days.stream().mapToLong(CalendarDaySummaryResponse::getDiaryEntryCount).sum();
            long totalEvents = days.stream().mapToLong(CalendarDaySummaryResponse::getEventCount).sum();
            long totalMeetings = days.stream().mapToLong(CalendarDaySummaryResponse::getMeetingCount).sum();
            long totalClasses = days.stream().mapToLong(CalendarDaySummaryResponse::getClassCount).sum();
            long totalQuickNotes = days.stream().mapToLong(CalendarDaySummaryResponse::getQuickNoteCount).sum();

            long workingDays = days.stream().mapToLong(d -> "WEEKDAY".equals(d.getDayType()) ? 1 : 0).sum();
            long weekendDays = days.stream().mapToLong(d -> "WEEKEND".equals(d.getDayType()) ? 1 : 0).sum();

            MonthlyCalendarSummaryResponse summary = MonthlyCalendarSummaryResponse.builder()
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .month(yearMonth)
                    .monthName(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                    .year(yearMonth.getYear())
                    .days(days)
                    .totalDaysWithDiaryEntries(totalDaysWithDiaryEntries)
                    .totalDaysWithEvents(totalDaysWithEvents)
                    .totalDaysWithMeetings(totalDaysWithMeetings)
                    .totalDaysWithClasses(totalDaysWithClasses)
                    .totalDaysWithQuickNotes(totalDaysWithQuickNotes)
                    .totalDiaryEntries(totalDiaryEntries)
                    .totalEvents(totalEvents)
                    .totalMeetings(totalMeetings)
                    .totalClasses(totalClasses)
                    .totalQuickNotes(totalQuickNotes)
                    .workingDays(workingDays)
                    .weekendDays(weekendDays)
                    .build();

            log.info("Monthly calendar summary retrieved successfully (test mode)");
            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            log.error("Error getting monthly calendar summary: {}", e.getMessage());
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

    private DiaryEntryResponse mapToResponse(Notes note) {
        try {
            // Get updated by teacher name if available
            String updatedByName = null;
            if (note.getUpdatedBy() != null) {
                try {
                    Long updatedById = Long.parseLong(note.getUpdatedBy());
                    Teacher updatedByTeacher = teacherRepository.findById(updatedById).orElse(null);
                    if (updatedByTeacher != null) {
                        updatedByName = updatedByTeacher.getFirstName() + " " + updatedByTeacher.getLastName();
                    }
                } catch (NumberFormatException e) {
                    // If updatedBy is not a number, skip
                }
            }

            return DiaryEntryResponse.builder()
                    .id(note.getId())
                    .entryTitle(note.getEntryTitle() != null ? note.getEntryTitle() : note.getTitle())
                    .entryContent(note.getEntryContent() != null ? note.getEntryContent() : note.getDescription())
                    .entryDate(note.getEntryDate())
                    .createdTime(note.getCreatedTime() != null ? 
                            LocalDateTime.parse(note.getCreatedTime().replace(" ", "T")) : LocalDateTime.now())
                    .updatedTime(note.getUpdatedTime() != null ? 
                            LocalDateTime.parse(note.getUpdatedTime().replace(" ", "T")) : LocalDateTime.now())
                    .teacherId(note.getTeacherId())
                    .teacherName(note.getTeacher() != null ? 
                            note.getTeacher().getFirstName() + " " + note.getTeacher().getLastName() : null)
                    .updatedBy(note.getUpdatedBy() != null ? Long.parseLong(note.getUpdatedBy()) : null)
                    .updatedByName(updatedByName)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping diary entry to response: {}", e.getMessage());
            // Return basic response if mapping fails
            return DiaryEntryResponse.builder()
                    .id(note.getId())
                    .entryTitle(note.getEntryTitle() != null ? note.getEntryTitle() : note.getTitle())
                    .entryContent(note.getEntryContent() != null ? note.getEntryContent() : note.getDescription())
                    .entryDate(note.getEntryDate())
                    .teacherId(note.getTeacherId())
                    .teacherName(note.getTeacher() != null ? 
                            note.getTeacher().getFirstName() + " " + note.getTeacher().getLastName() : null)
                    .updatedBy(note.getUpdatedBy() != null ? Long.parseLong(note.getUpdatedBy()) : null)
                    .build();
        }
    }
}
