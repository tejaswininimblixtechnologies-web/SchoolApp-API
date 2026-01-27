package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.QuickNote;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.QuickNoteRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Request.QuickNoteRequest;
import com.nimblix.SchoolPEPProject.Response.QuickNoteResponse;
import com.nimblix.SchoolPEPProject.Service.QuickNoteService;
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
public class QuickNoteServiceImpl implements QuickNoteService {

    private final QuickNoteRepository quickNoteRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<QuickNoteResponse> createQuickNote(QuickNoteRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Creating quick note for teacher: {} on date: {}", teacher.getId(), request.getNoteDate());

            // For now, create a simple response without database operations
            QuickNoteResponse response = QuickNoteResponse.builder()
                    .id(1L)
                    .noteDate(request.getNoteDate())
                    .noteText(request.getNoteText())
                    .priority(request.getPriority() != null ? request.getPriority() : "NORMAL")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build();

            log.info("Quick note created successfully (test mode)");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error creating quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<QuickNoteResponse> updateQuickNote(Long noteId, QuickNoteRequest request) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> deleteQuickNote(Long noteId) {
        try {
            // For now, simulate soft delete since we don't have database operations
            log.info("Quick note soft deleted successfully: {} (test mode)", noteId);
            return ResponseEntity.ok("Quick note deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<QuickNoteResponse> getQuickNoteById(Long noteId) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getTeacherNotes() {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDate(LocalDate noteDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Getting quick notes for teacher: {} on date: {}", teacher.getId(), noteDate);

            // For now, return sample data for testing
            List<QuickNoteResponse> sampleNotes = List.of(
                QuickNoteResponse.builder()
                    .id(1L)
                    .noteDate(noteDate)
                    .noteText("Remember to bring science project materials for tomorrow's class")
                    .priority("HIGH")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build(),
                QuickNoteResponse.builder()
                    .id(2L)
                    .noteDate(noteDate)
                    .noteText("Submit student progress reports to principal by end of day")
                    .priority("MEDIUM")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build(),
                QuickNoteResponse.builder()
                    .id(3L)
                    .noteDate(noteDate)
                    .noteText("Prepare lesson plan for next week's algebra topic")
                    .priority("NORMAL")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build()
            );

            return ResponseEntity.ok(sampleNotes);

        } catch (Exception e) {
            log.error("Error getting quick notes by date: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDateRange(LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getUpcomingNotes() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            log.info("Getting upcoming quick notes for teacher: {}", teacher.getId());

            // For now, return sample upcoming notes
            List<QuickNoteResponse> upcomingNotes = List.of(
                QuickNoteResponse.builder()
                    .id(4L)
                    .noteDate(today.plusDays(1))
                    .noteText("Parent-teacher meeting scheduled for tomorrow at 10 AM")
                    .priority("HIGH")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build(),
                QuickNoteResponse.builder()
                    .id(5L)
                    .noteDate(today.plusDays(3))
                    .noteText("Science exhibition preparations - check project requirements")
                    .priority("MEDIUM")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build()
            );

            return ResponseEntity.ok(upcomingNotes);

        } catch (Exception e) {
            log.error("Error getting upcoming quick notes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getTodayNotes() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            log.info("Getting today's quick notes for teacher: {}", teacher.getId());

            // For now, return sample today's notes
            List<QuickNoteResponse> todayNotes = List.of(
                QuickNoteResponse.builder()
                    .id(6L)
                    .noteDate(today)
                    .noteText("Morning assembly duty - be present by 8:30 AM")
                    .priority("HIGH")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build(),
                QuickNoteResponse.builder()
                    .id(7L)
                    .noteDate(today)
                    .noteText("Collect homework assignments from 10th grade")
                    .priority("NORMAL")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build()
            );

            return ResponseEntity.ok(todayNotes);

        } catch (Exception e) {
            log.error("Error getting today's quick notes: {}", e.getMessage());
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
