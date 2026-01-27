package com.nimblix.SchoolPEPProject.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EventRequest {

    @NotBlank(message = "Event title is required")
    private String eventTitle;

    @NotNull(message = "Event date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventDate;

    private String description;

    private String applicableClasses;

    private Boolean isSchoolWide = false;
}
