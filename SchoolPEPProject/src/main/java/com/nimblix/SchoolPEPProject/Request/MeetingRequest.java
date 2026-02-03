package com.nimblix.SchoolPEPProject.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class MeetingRequest {

    @NotBlank(message = "Meeting title is required")
    private String meetingTitle;

    @NotNull(message = "Meeting date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate meetingDate;

    @NotBlank(message = "Meeting time is required")
    private String meetingTime;

    private String participants;

    private String meetingNotes;
}
