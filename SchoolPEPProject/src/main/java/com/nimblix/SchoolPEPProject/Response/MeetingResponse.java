package com.nimblix.SchoolPEPProject.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class MeetingResponse {

    private Long id;

    private String meetingTitle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate meetingDate;

    private String meetingTime;

    private String participants;

    private String meetingNotes;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    // Teacher information
    private Long teacherId;
    private String teacherName;
    private Long updatedBy;
    private String updatedByName;
}
