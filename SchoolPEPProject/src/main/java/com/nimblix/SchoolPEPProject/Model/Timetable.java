package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "timetables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "section")
    private String section;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "day_of_week")
    private String dayOfWeek; // MONDAY, TUESDAY, etc.

    @Column(name = "period_number")
    private Integer periodNumber;

    @Column(name = "subject")
    private String subject;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "start_time")
    private String startTime; // "09:00 AM"

    @Column(name = "end_time")
    private String endTime; // "09:45 AM"

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = java.time.LocalDateTime.now().toString();
    }
}
