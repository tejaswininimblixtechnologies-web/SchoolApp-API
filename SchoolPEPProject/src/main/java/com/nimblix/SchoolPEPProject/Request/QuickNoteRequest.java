package com.nimblix.SchoolPEPProject.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class QuickNoteRequest {

    @NotNull(message = "Note date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate noteDate;

    @NotBlank(message = "Note text is required")
    private String noteText;

    private String priority = "NORMAL"; // HIGH, MEDIUM, NORMAL, LOW
}
