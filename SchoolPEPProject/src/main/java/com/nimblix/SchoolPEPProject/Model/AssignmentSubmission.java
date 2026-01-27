package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assignment_submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "submission_date")
    private String submissionDate;

    @Column(name = "submission_status")
    private String submissionStatus; // SUBMITTED, PENDING, LATE

    @Column(name = "evaluation_status")
    private String evaluationStatus; // EVALUATED, PENDING_EVALUATION

    @Column(name = "marks_obtained")
    private Integer marksObtained;

    @Column(name = "teacher_feedback", columnDefinition = "TEXT")
    private String teacherFeedback;

    @Column(name = "evaluated_by")
    private String evaluatedBy;

    @Column(name = "evaluated_date")
    private String evaluatedDate;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = java.time.LocalDateTime.now().toString();
        updatedTime = createdTime;
        if (submissionDate == null) {
            submissionDate = java.time.LocalDate.now().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = java.time.LocalDateTime.now().toString();
    }
}
