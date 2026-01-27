package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.DiaryEntryRequest;
import com.nimblix.SchoolPEPProject.Response.DiaryEntryResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DiaryEntryService {

    // CRUD Operations
    ResponseEntity<DiaryEntryResponse> createEntry(DiaryEntryRequest request);
    ResponseEntity<DiaryEntryResponse> updateEntry(Long entryId, DiaryEntryRequest request);
    ResponseEntity<String> deleteEntry(Long entryId);
    ResponseEntity<DiaryEntryResponse> getEntryById(Long entryId);
    ResponseEntity<List<DiaryEntryResponse>> getTeacherEntries();
}
