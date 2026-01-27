package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.QuickNoteRequest;
import com.nimblix.SchoolPEPProject.Response.QuickNoteResponse;
import com.nimblix.SchoolPEPProject.Service.QuickNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/quick-notes")
@RequiredArgsConstructor
@Slf4j
public class QuickNoteController {

    private final QuickNoteService quickNoteService;

    // Add Quick Note
    @PostMapping("/add")
    public ResponseEntity<QuickNoteResponse> createQuickNote(@Valid @RequestBody QuickNoteRequest request) {
        log.info("Creating quick note: {}", request.getNoteText());
        return quickNoteService.createQuickNote(request);
    }

    // Get Quick Notes by Date
    @GetMapping("/add/date/{noteDate}")
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDate(@PathVariable LocalDate noteDate) {
        log.info("Getting quick notes for date: {}", noteDate);
        return quickNoteService.getNotesByDate(noteDate);
    }

    // Get Today's Quick Notes
    @GetMapping("/today")
    public ResponseEntity<List<QuickNoteResponse>> getTodayNotes() {
        log.info("Getting today's quick notes");
        return quickNoteService.getTodayNotes();
    }

    // Get Upcoming Quick Notes
    @GetMapping("/upcoming")
    public ResponseEntity<List<QuickNoteResponse>> getUpcomingNotes() {
        log.info("Getting upcoming quick notes");
        return quickNoteService.getUpcomingNotes();
    }

    // Get All Teacher Quick Notes
    @GetMapping("/add")
    public ResponseEntity<List<QuickNoteResponse>> getTeacherNotes() {
        log.info("Getting all teacher quick notes");
        return quickNoteService.getTeacherNotes();
    }

    // Get Quick Note by ID
    @GetMapping("/add/{noteId}")
    public ResponseEntity<QuickNoteResponse> getQuickNoteById(@PathVariable Long noteId) {
        log.info("Getting quick note: {}", noteId);
        return quickNoteService.getQuickNoteById(noteId);
    }

    // Update Quick Note
    @PutMapping("/add/{noteId}")
    public ResponseEntity<QuickNoteResponse> updateQuickNote(
            @PathVariable Long noteId,
            @Valid @RequestBody QuickNoteRequest request) {
        log.info("Updating quick note: {}", noteId);
        return quickNoteService.updateQuickNote(noteId, request);
    }

    // Delete Quick Note
    @DeleteMapping("/add/{noteId}")
    public ResponseEntity<String> deleteQuickNote(@PathVariable Long noteId) {
        log.info("Deleting quick note: {}", noteId);
        return quickNoteService.deleteQuickNote(noteId);
    }

    // Get Quick Notes by Date Range
    @GetMapping("/add/range")
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Getting quick notes from {} to {}", startDate, endDate);
        return quickNoteService.getNotesByDateRange(startDate, endDate);
    }

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Quick note controller is working!");
        return ResponseEntity.ok("Quick Note API is working!");
    }
}
