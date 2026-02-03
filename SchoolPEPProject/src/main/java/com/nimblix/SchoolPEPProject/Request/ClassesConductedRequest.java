package com.nimblix.SchoolPEPProject.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ClassesConductedRequest {

    @NotNull(message = "Class date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate classDate;

    @NotNull(message = "Classroom is required")
    private Long classroomId;

    private String section;

    @NotNull(message = "Subject is required")
    private Long subjectId;

    @NotBlank(message = "Period duration is required")
    private String periodDuration;

    private String remarks;
}
