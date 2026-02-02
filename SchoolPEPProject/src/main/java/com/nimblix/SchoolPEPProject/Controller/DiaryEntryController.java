package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.DiaryEntryRequest;
import com.nimblix.SchoolPEPProject.Request.QuickNoteRequest;
import com.nimblix.SchoolPEPProject.Response.DiaryEntryResponse;
import com.nimblix.SchoolPEPProject.Response.QuickNoteResponse;
import com.nimblix.SchoolPEPProject.Service.DiaryEntryService;
import com.nimblix.SchoolPEPProject.Service.QuickNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
@Slf4j
public class DiaryEntryController {

    private final DiaryEntryService diaryEntryService;
    private final QuickNoteService quickNoteService;

    // ========== DIARY ENTRY ENDPOINTS ==========

    // Add Diary Entry
    @PostMapping("/entries")
    public ResponseEntity<DiaryEntryResponse> createEntry(@Valid @RequestBody DiaryEntryRequest request) {
        log.info("Creating diary entry: {}", request.getEntryTitle());
        return diaryEntryService.createEntry(request);
    }

    // Update Diary Entry
    @PutMapping("/entries/{entryId}")
    public ResponseEntity<DiaryEntryResponse> updateEntry(
            @PathVariable Long entryId,
            @Valid @RequestBody DiaryEntryRequest request) {
        log.info("Updating diary entry: {}", entryId);
        return diaryEntryService.updateEntry(entryId, request);
    }

    // Delete Diary Entry
    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<String> deleteEntry(@PathVariable Long entryId) {
        log.info("Deleting diary entry: {}", entryId);
        return diaryEntryService.deleteEntry(entryId);
    }

    // Get Diary Entry by ID
    @GetMapping("/entries/{entryId}")
    public ResponseEntity<DiaryEntryResponse> getEntryById(@PathVariable Long entryId) {
        log.info("Getting diary entry: {}", entryId);
        return diaryEntryService.getEntryById(entryId);
    }

    // Get All Teacher Diary Entries
    @GetMapping("/entries")
    public ResponseEntity<List<DiaryEntryResponse>> getTeacherEntries() {
        log.info("Getting all teacher diary entries");
        return diaryEntryService.getTeacherEntries();
    }

    // ========== QUICK NOTES ENDPOINTS ==========

    // Add Quick Note
    @PostMapping("/notes/add")
    public ResponseEntity<QuickNoteResponse> createQuickNote(@Valid @RequestBody QuickNoteRequest request) {
        log.info("Creating quick note: {}", request.getNoteText());
        return quickNoteService.createQuickNote(request);
    }

    // Update Quick Note
    @PutMapping("/notes/add/{noteId}")
    public ResponseEntity<QuickNoteResponse> updateQuickNote(
            @PathVariable Long noteId,
            @Valid @RequestBody QuickNoteRequest request) {
        log.info("Updating quick note: {}", noteId);
        return quickNoteService.updateQuickNote(noteId, request);
    }

    // Delete Quick Note
    @DeleteMapping("/notes/add/{noteId}")
    public ResponseEntity<String> deleteQuickNote(@PathVariable Long noteId) {
        log.info("Deleting quick note: {}", noteId);
        return quickNoteService.deleteQuickNote(noteId);
    }

    // Get Quick Note by ID
    @GetMapping("/notes/add/{noteId}")
    public ResponseEntity<QuickNoteResponse> getQuickNoteById(@PathVariable Long noteId) {
        log.info("Getting quick note: {}", noteId);
        return quickNoteService.getQuickNoteById(noteId);
    }

    // Get All Teacher Quick Notes
    @GetMapping("/notes/add")
    public ResponseEntity<List<QuickNoteResponse>> getTeacherNotes() {
        log.info("Getting all teacher quick notes");
        return quickNoteService.getTeacherNotes();
    }

    // Get Quick Notes by Date
    @GetMapping("/notes/add/date/{noteDate}")
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDate(@PathVariable LocalDate noteDate) {
        log.info("Getting quick notes for date: {}", noteDate);
        return quickNoteService.getNotesByDate(noteDate);
    }

    // Get Quick Notes by Date Range
    @GetMapping("/notes/add/range")
    public ResponseEntity<List<QuickNoteResponse>> getNotesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Getting quick notes from {} to {}", startDate, endDate);
        return quickNoteService.getNotesByDateRange(startDate, endDate);
    }

    // Get Upcoming Quick Notes
    @GetMapping("/notes/upcoming")
    public ResponseEntity<List<QuickNoteResponse>> getUpcomingNotes() {
        log.info("Getting upcoming quick notes");
        return quickNoteService.getUpcomingNotes();
    }

    // Get Today's Quick Notes
    @GetMapping("/notes/today")
    public ResponseEntity<List<QuickNoteResponse>> getTodayNotes() {
        log.info("Getting today's quick notes");
        return quickNoteService.getTodayNotes();
    }

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Diary controller is working!");
        return ResponseEntity.ok("Diary API is working!");
    }
}
