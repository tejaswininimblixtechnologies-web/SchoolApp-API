package com.nimblix.SchoolPEPProject.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ClassesConductedResponse {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate classDate;

    private Long classroomId;
    private String classroomName;
    private String section;

    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    private String periodDuration;
    private String remarks;
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
