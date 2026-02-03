package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "student_id")
    private Long studentId;

    // ========== QUICKNOTE FIELDS ==========
    
    @Column(name = "note_date")
    private LocalDate noteDate;

    @Column(name = "priority")
    private String priority; // HIGH, MEDIUM, LOW

    @Column(name = "note_status")
    private String noteStatus; // ACTIVE, INACTIVE, DELETED

    @Column(name = "note_type")
    private String noteType; // QUICK, GENERAL, ACADEMIC, DIARY

    @Column(name = "updated_by")
    private String updatedBy;

    // ========== DIARY ENTRY FIELDS ==========
    
    @Column(name = "entry_title")
    private String entryTitle;  // For diary entries

    @Column(name = "entry_content", columnDefinition = "TEXT")
    private String entryContent;  // For diary entries

    @Column(name = "entry_date")
    private LocalDateTime entryDate;  // For diary entries

    @Column(name = "entry_status")
    private String entryStatus = "ACTIVE";  // For diary entries

    // ========== EXISTING TIMESTAMP FIELDS ==========
    
    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    // ========== TEACHER RELATIONSHIP ==========
    
    @ManyToOne
    @JoinColumn(name = "teacher_id", insertable = false, updatable = false)
    private Teacher teacher;

    @PrePersist
    protected void onCreate(){
        createdTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        updatedTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        
        // Set default values for QuickNote fields
        if (noteDate == null) {
            noteDate = LocalDate.now();
        }
        if (priority == null) {
            priority = "MEDIUM";
        }
        if (noteStatus == null) {
            noteStatus = "ACTIVE";
        }
        if (noteType == null) {
            noteType = "GENERAL";
        }
        
        // Set default values for Diary Entry fields
        if (entryStatus == null) {
            entryStatus = "ACTIVE";
        }
        
        // Auto-populate title/description for diary entries
        if ("DIARY".equals(noteType)) {
            if (title == null && entryTitle != null) {
                title = entryTitle;
            }
            if (description == null && entryContent != null) {
                description = entryContent;
            }
        }
    }

    @PreUpdate
    protected void onUpdate(){
        updatedTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        
        // Sync diary entry fields with main fields for diary entries
        if ("DIARY".equals(noteType)) {
            if (entryTitle != null) {
                title = entryTitle;
            }
            if (entryContent != null) {
                description = entryContent;
            }
        }
    }
}