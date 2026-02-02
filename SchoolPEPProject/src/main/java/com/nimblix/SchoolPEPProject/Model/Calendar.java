package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Enum.EventType;
import com.nimblix.SchoolPEPProject.Enum.RepeatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "event_title", nullable = false)
    private String eventTitle;

    @Column(name = "event_description", columnDefinition = "TEXT")
    private String eventDescription;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_type")
    private RepeatType repeatType;

    @Column(name = "location")
    private String location;

    @Column(name = "is_all_day")
    private Boolean isAllDay = false;

    @Column(name = "reminder_minutes")
    private Integer reminderMinutes;

    @Column(name = "color")
    private String color;

    // ========== EVENT FIELDS ==========
    
    @Column(name = "event_date")
    private LocalDate eventDate;  // For simple date events (no time)

    @Column(name = "school_wide")
    private Boolean schoolWide = false;  // School-wide flag

    @Column(name = "applicable_classes", columnDefinition = "TEXT")
    private String applicableClasses;  // JSON array of class IDs

    @Column(name = "event_status")
    private String eventStatus;  // ACTIVE, INACTIVE, DELETED

    @Column(name = "updated_by")
    private String updatedBy;

    // ========== MEETING FIELDS ==========
    
    @Column(name = "meeting_title")
    private String meetingTitle;  // For meetings

    @Column(name = "meeting_date")
    private LocalDate meetingDate;  // For meetings

    @Column(name = "meeting_time")
    private String meetingTime;  // For meetings

    @Column(name = "participants", columnDefinition = "TEXT")
    private String participants;  // For meetings

    @Column(name = "meeting_notes", columnDefinition = "TEXT")
    private String meetingNotes;  // For meetings

    @Column(name = "meeting_status")
    private String meetingStatus = "ACTIVE";  // For meetings

    // ========== TEACHER RELATIONSHIP ==========
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // Note: teacherId is accessed through teacher.getId() to avoid duplicate column mapping

    // Helper method to get teacher ID (for backward compatibility)
    public Long getTeacherId() {
        return teacher != null ? teacher.getId() : null;
    }

    // Helper method to set teacher ID (for backward compatibility)
    public void setTeacherId(Long teacherId) {
        // This method is for backward compatibility only
        // The actual teacher relationship should be set via setTeacher()
    }

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subject;

    // ========== TIMESTAMP FIELDS ==========
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdTime = now;
        updatedTime = now;
        
        // Set default values for Event fields
        if (eventStatus == null) {
            eventStatus = "ACTIVE";
        }
        if (schoolWide == null) {
            schoolWide = false;
        }
        if (isAllDay == null) {
            isAllDay = false;
        }
        
        // Set default values for Meeting fields
        if (meetingStatus == null) {
            meetingStatus = "ACTIVE";
        }
        
        // If eventDate is not set but startDateTime is, extract date from startDateTime
        if (eventDate == null && startDateTime != null) {
            eventDate = startDateTime.toLocalDate();
        }
        
        // Auto-populate fields for meetings
        if (eventType == EventType.MEETING) {
            if (eventTitle != null && meetingTitle == null) {
                meetingTitle = eventTitle;
            }
            if (eventDescription != null && meetingNotes == null) {
                meetingNotes = eventDescription;
            }
            if (eventDate != null && meetingDate == null) {
                meetingDate = eventDate;
            }
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
        
        // Update eventDate if startDateTime changed
        if (startDateTime != null) {
            eventDate = startDateTime.toLocalDate();
        }
        
        // Sync fields for meetings
        if (eventType == EventType.MEETING) {
            if (meetingTitle != null) {
                eventTitle = meetingTitle;
            }
            if (meetingNotes != null) {
                eventDescription = meetingNotes;
            }
            if (meetingDate != null) {
                eventDate = meetingDate;
            }
        }
    }

    // Helper method to set status based on event type
    public void setStatus(String status) {
        if (eventType == EventType.MEETING) {
            this.meetingStatus = status;
        } else {
            this.eventStatus = status;
        }
    }

    // Helper method to get status based on event type
    public String getStatus() {
        if (eventType == EventType.MEETING) {
            return this.meetingStatus;
        } else {
            return this.eventStatus;
        }
    }
}
