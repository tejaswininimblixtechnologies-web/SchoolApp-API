package com.nimblix.SchoolPEPProject.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class DiaryEntryRequest {

    @NotBlank(message = "Entry title is required")
    private String entryTitle;

    @NotBlank(message = "Entry content is required")
    private String entryContent;

    @NotNull(message = "Entry date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryDate;
}
