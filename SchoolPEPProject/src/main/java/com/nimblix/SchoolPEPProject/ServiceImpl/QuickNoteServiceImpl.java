package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Notes;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.NotesRepository;
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
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuickNoteServiceImpl implements QuickNoteService {

    private final NotesRepository notesRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<QuickNoteResponse> createQuickNote(QuickNoteRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Creating quick note for teacher: {} on date: {}", teacher.getId(), request.getNoteDate());

            Notes note = new Notes();
            note.setNoteType("QUICK");
            note.setNoteDate(request.getNoteDate());
            note.setDescription(request.getNoteText());
            note.setPriority(request.getPriority() != null ? request.getPriority() : "MEDIUM");
            note.setNoteStatus("ACTIVE");
            note.setTeacher(teacher);
            
            // Set main fields for consistency
            note.setTitle("Quick Note");
            note.setNoteDate(request.getNoteDate());
            note.setUpdatedBy(teacher.getId().toString());

            Notes savedNote = notesRepository.save(note);
            log.info("Quick note created successfully: {}", savedNote.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedNote));

        } catch (Exception e) {
            log.error("Error creating quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<QuickNoteResponse> updateQuickNote(Long noteId, QuickNoteRequest request) {
        try {
            Notes note = notesRepository.findById(noteId)
                    .orElseThrow(() -> new RuntimeException("Quick note not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the note belongs to the authenticated teacher
            if (!note.getTeacherId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update note fields
            note.setNoteDate(request.getNoteDate());
            note.setDescription(request.getNoteText());
            note.setPriority(request.getPriority());
            note.setUpdatedBy(teacher.getId().toString());

            Notes updatedNote = notesRepository.save(note);
            log.info("Quick note updated successfully: {}", updatedNote.getId());

            return ResponseEntity.ok(mapToResponse(updatedNote));

        } catch (Exception e) {
            log.error("Error updating quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteQuickNote(Long noteId) {
        try {
            Notes note = notesRepository.findById(noteId)
                    .orElseThrow(() -> new RuntimeException("Quick note not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the note belongs to the authenticated teacher
            if (!note.getTeacherId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Soft delete - change status to DELETED
            note.setNoteStatus("DELETED");
            note.setUpdatedBy(teacher.getId().toString());
            
            notesRepository.save(note);
            log.info("Quick note soft deleted successfully: {}", noteId);

            return ResponseEntity.ok("Quick note deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<QuickNoteResponse> getQuickNoteById(Long noteId) {
        try {
            Notes note = notesRepository.findById(noteId)
                    .orElseThrow(() -> new RuntimeException("Quick note not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the note belongs to the authenticated teacher
            if (!note.getTeacherId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(mapToResponse(note));

        } catch (Exception e) {
            log.error("Error getting quick note: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getTeacherNotes() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Notes> notes = notesRepository.findByTeacherIdAndNoteType(teacher.getId(), "QUICK");
            List<QuickNoteResponse> responses = notes.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting teacher notes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDate(LocalDate noteDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Notes> notes = notesRepository.findByTeacherIdAndNoteDate(teacher.getId(), noteDate);
            List<QuickNoteResponse> responses = notes.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting notes by date: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<Notes> notes = notesRepository.findByTeacherIdAndNoteDateBetween(teacher.getId(), startDate, endDate);
            List<QuickNoteResponse> responses = notes.stream()
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting notes by date range: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getUpcomingNotes() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            List<Notes> notes = notesRepository.findUpcomingNotes(today);
            List<QuickNoteResponse> responses = notes.stream()
                    .filter(note -> note.getTeacherId().equals(teacher.getId()))
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting upcoming notes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<QuickNoteResponse>> getTodayNotes() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            List<Notes> notes = notesRepository.findTodayNotes(today);
            List<QuickNoteResponse> responses = notes.stream()
                    .filter(note -> note.getTeacherId().equals(teacher.getId()))
                    .map(this::mapToResponse)
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting today's notes: {}", e.getMessage());
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

    private QuickNoteResponse mapToResponse(Notes note) {
        try {
            return QuickNoteResponse.builder()
                    .id(note.getId())
                    .noteDate(note.getNoteDate())
                    .noteText(note.getDescription())
                    .priority(note.getPriority())
                    .status(note.getNoteStatus())
                    .teacherId(note.getTeacherId())
                    .teacherName(note.getTeacher() != null ? 
                            note.getTeacher().getFirstName() + " " + note.getTeacher().getLastName() : null)
                    .createdTime(note.getCreatedTime() != null ? 
                            LocalDateTime.parse(note.getCreatedTime().replace(" ", "T")) : LocalDateTime.now())
                    .updatedTime(note.getUpdatedTime() != null ? 
                            LocalDateTime.parse(note.getUpdatedTime().replace(" ", "T")) : LocalDateTime.now())
                    .updatedBy(note.getUpdatedBy() != null ? Long.parseLong(note.getUpdatedBy()) : null)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping note to quick note response: {}", e.getMessage());
            // Return basic response if mapping fails
            return QuickNoteResponse.builder()
                    .id(note.getId())
                    .noteDate(note.getNoteDate())
                    .noteText(note.getDescription())
                    .priority(note.getPriority())
                    .status(note.getNoteStatus())
                    .teacherId(note.getTeacherId())
                    .teacherName(note.getTeacher() != null ? 
                            note.getTeacher().getFirstName() + " " + note.getTeacher().getLastName() : null)
                    .build();
        }
    }
}
