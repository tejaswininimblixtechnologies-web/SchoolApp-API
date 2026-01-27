package com.nimblix.SchoolPEPProject.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nimblix.SchoolPEPProject.Enum.EventType;
import com.nimblix.SchoolPEPProject.Enum.RepeatType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CalendarEventResponse {

    private Long id;
    private String eventTitle;
    private String eventDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;

    private EventType eventType;
    private RepeatType repeatType;
    private String location;
    private Boolean isAllDay;
    private Integer reminderMinutes;
    private String color;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    // Related entities
    private Long teacherId;
    private String teacherName;
    private Long classroomId;
    private String classroomName;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
}
