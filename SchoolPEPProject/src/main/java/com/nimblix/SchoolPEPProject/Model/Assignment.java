package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "section")
    private String section;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "subject")
    private String subject;

    @Column(name = "assignment_title")
    private String assignmentTitle;

    @Column(name = "assignment_description", columnDefinition = "TEXT")
    private String assignmentDescription;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "assigned_date")
    private String assignedDate;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "allowed_file_types")
    private String allowedFileTypes; // "pdf,doc,docx,ppt,pptx"

    @Column(name = "max_file_size")
    private Long maxFileSize; // in bytes

    @Column(name = "created_by")
    private String createdBy; // Teacher name

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = java.time.LocalDateTime.now().toString();
        updatedTime = createdTime;
        if (assignedDate == null) {
            assignedDate = java.time.LocalDate.now().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = java.time.LocalDateTime.now().toString();
    }
}
