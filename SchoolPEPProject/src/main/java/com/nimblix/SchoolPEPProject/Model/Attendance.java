package com.nimblix.SchoolPEPProject.Model;


import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "attendance_record",
uniqueConstraints = @UniqueConstraint(
        columnNames = {"school_id", "student_id", "attendance_date"}
)
)
@Getter
@Setter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id",nullable = false)
    private Long studentId;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status",nullable = false)
    private AttendanceStatus attendanceStatus;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "school_id",nullable = false)
    private Long schoolId;

    @Column(name = "class_id",nullable = false)
    private Long classId;

    @Column(name = "section",nullable = false)
    private String section;



    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedTime = LocalDateTime.now();
    }

}