package com.nimblix.SchoolPEPProject.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nimblix.SchoolPEPProject.Enum.EventType;
import com.nimblix.SchoolPEPProject.Enum.RepeatType;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CalendarEventRequest {

    @NotBlank(message = "Event title is required")
    private String eventTitle;

    private String eventDescription;

    @NotNull(message = "Start date and time is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;

    @NotNull(message = "Event type is required")
    private EventType eventType;

    private RepeatType repeatType = RepeatType.NONE;

    private String location;

    private Boolean isAllDay = false;

    private Integer reminderMinutes;

    private String color;

    private Long classroomId;

    private Long subjectId;
}
