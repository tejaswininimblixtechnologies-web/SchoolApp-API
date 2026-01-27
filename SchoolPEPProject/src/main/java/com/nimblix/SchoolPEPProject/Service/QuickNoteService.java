package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.QuickNoteRequest;
import com.nimblix.SchoolPEPProject.Response.QuickNoteResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface QuickNoteService {

    // CRUD Operations
    ResponseEntity<QuickNoteResponse> createQuickNote(QuickNoteRequest request);
    ResponseEntity<QuickNoteResponse> updateQuickNote(Long noteId, QuickNoteRequest request);
    ResponseEntity<String> deleteQuickNote(Long noteId);
    ResponseEntity<QuickNoteResponse> getQuickNoteById(Long noteId);

    // Teacher Notes Operations
    ResponseEntity<List<QuickNoteResponse>> getTeacherNotes();
    ResponseEntity<List<QuickNoteResponse>> getNotesByDate(LocalDate noteDate);
    ResponseEntity<List<QuickNoteResponse>> getNotesByDateRange(LocalDate startDate, LocalDate endDate);

    // Upcoming Notes
    ResponseEntity<List<QuickNoteResponse>> getUpcomingNotes();
    ResponseEntity<List<QuickNoteResponse>> getTodayNotes();
}
