package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "timetable_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimetableNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "section")
    private String section;

    @Column(name = "subject")
    private String subject;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "period_number")
    private Integer periodNumber;

    @Column(name = "note_title")
    private String noteTitle;

    @Column(name = "note_description")
    private String noteDescription;

    @Column(name = "uploaded_by")
    private String uploadedBy; // Teacher name or Admin

    @Column(name = "uploaded_date")
    private String uploadedDate;

    @ManyToOne
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = java.time.LocalDateTime.now().toString();
        updatedTime = createdTime;
        if (uploadedDate == null) {
            uploadedDate = java.time.LocalDate.now().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = java.time.LocalDateTime.now().toString();
    }
}
