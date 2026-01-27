package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.DiaryEntry;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.DiaryEntryRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Request.DiaryEntryRequest;
import com.nimblix.SchoolPEPProject.Response.DiaryEntryResponse;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiaryEntryServiceImpl implements DiaryEntryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<DiaryEntryResponse> createEntry(DiaryEntryRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();

            DiaryEntry diaryEntry = new DiaryEntry();
            diaryEntry.setEntryTitle(request.getEntryTitle());
            diaryEntry.setEntryContent(request.getEntryContent());
            diaryEntry.setEntryDate(request.getEntryDate());
            diaryEntry.setTeacher(teacher);

            DiaryEntry savedEntry = diaryEntryRepository.save(diaryEntry);
            log.info("Diary entry created successfully: {}", savedEntry.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedEntry));

        } catch (Exception e) {
            log.error("Error creating diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<DiaryEntryResponse> updateEntry(Long entryId, DiaryEntryRequest request) {
        try {
            DiaryEntry diaryEntry = diaryEntryRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the entry belongs to the authenticated teacher
            if (!diaryEntry.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update diary summary
            diaryEntry.setEntryTitle(request.getEntryTitle());
            diaryEntry.setEntryContent(request.getEntryContent());
            diaryEntry.setEntryDate(request.getEntryDate());
            
            // Track who updated the entry
            diaryEntry.setUpdatedBy(teacher.getId());

            DiaryEntry updatedEntry = diaryEntryRepository.save(diaryEntry);
            log.info("Diary entry updated successfully: {}", updatedEntry.getId());

            return ResponseEntity.ok(mapToResponse(updatedEntry));

        } catch (Exception e) {
            log.error("Error updating diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEntry(Long entryId) {
        try {
            DiaryEntry diaryEntry = diaryEntryRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the entry belongs to the authenticated teacher
            if (!diaryEntry.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Soft delete - change status to DELETED instead of removing from database
            diaryEntry.setStatus("DELETED");
            diaryEntry.setUpdatedBy(teacher.getId());
            
            diaryEntryRepository.save(diaryEntry);
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
            DiaryEntry diaryEntry = diaryEntryRepository.findById(entryId)
                    .orElseThrow(() -> new RuntimeException("Diary entry not found"));

            Teacher teacher = getAuthenticatedTeacher();

            // Check if the entry belongs to the authenticated teacher
            if (!diaryEntry.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(mapToResponse(diaryEntry));

        } catch (Exception e) {
            log.error("Error getting diary entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<DiaryEntryResponse>> getTeacherEntries() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            List<DiaryEntry> entries = diaryEntryRepository.findByTeacherId(teacher.getId());
            List<DiaryEntryResponse> responses = entries.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting teacher diary entries: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Helper methods
    private Teacher getAuthenticatedTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return teacherRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    private DiaryEntryResponse mapToResponse(DiaryEntry diaryEntry) {
        try {
            // Get updated by teacher name if available
            String updatedByName = null;
            if (diaryEntry.getUpdatedBy() != null) {
                Teacher updatedByTeacher = teacherRepository.findById(diaryEntry.getUpdatedBy()).orElse(null);
                if (updatedByTeacher != null) {
                    updatedByName = updatedByTeacher.getFirstName() + " " + updatedByTeacher.getLastName();
                }
            }

            return DiaryEntryResponse.builder()
                    .id(diaryEntry.getId())
                    .entryTitle(diaryEntry.getEntryTitle())
                    .entryContent(diaryEntry.getEntryContent())
                    .entryDate(diaryEntry.getEntryDate())
                    .createdTime(diaryEntry.getCreatedTime())
                    .updatedTime(diaryEntry.getUpdatedTime())
                    .teacherId(diaryEntry.getTeacher() != null ? diaryEntry.getTeacher().getId() : null)
                    .teacherName(diaryEntry.getTeacher() != null ? 
                            diaryEntry.getTeacher().getFirstName() + " " + diaryEntry.getTeacher().getLastName() : null)
                    .updatedBy(diaryEntry.getUpdatedBy())
                    .updatedByName(updatedByName)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping diary entry to response: {}", e.getMessage());
            // Return basic response if mapping fails
            return DiaryEntryResponse.builder()
                    .id(diaryEntry.getId())
                    .entryTitle(diaryEntry.getEntryTitle())
                    .entryContent(diaryEntry.getEntryContent())
                    .entryDate(diaryEntry.getEntryDate())
                    .createdTime(diaryEntry.getCreatedTime())
                    .updatedTime(diaryEntry.getUpdatedTime())
                    .teacherId(diaryEntry.getTeacher() != null ? diaryEntry.getTeacher().getId() : null)
                    .teacherName(diaryEntry.getTeacher() != null ? 
                            diaryEntry.getTeacher().getFirstName() + " " + diaryEntry.getTeacher().getLastName() : null)
                    .updatedBy(diaryEntry.getUpdatedBy())
                    .build();
        }
    }

    }
