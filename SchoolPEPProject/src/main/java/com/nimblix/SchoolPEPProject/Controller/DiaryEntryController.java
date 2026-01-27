package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.DiaryEntryRequest;
import com.nimblix.SchoolPEPProject.Response.DiaryEntryResponse;
import com.nimblix.SchoolPEPProject.Service.DiaryEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
@Slf4j
public class DiaryEntryController {

    private final DiaryEntryService diaryEntryService;

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

    // Test endpoint for classes path
    @GetMapping("/test-classes")
    public ResponseEntity<String> testClassesPath() {
        log.info("Testing classes path from diary controller");
        return ResponseEntity.ok("Classes path is working!");
    }
}
