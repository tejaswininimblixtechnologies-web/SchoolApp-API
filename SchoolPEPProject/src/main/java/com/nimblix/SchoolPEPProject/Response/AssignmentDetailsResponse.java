package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssignmentDetailsResponse {

    private Long assignmentId;
    private String subject;
    private String assignmentTitle;
    private String assignmentDescription;
    private String instructions;
    private String allowedFileTypes;
    private Long maxFileSize;
    private String assignedDate;
    private String dueDate;
    private String createdBy;
}
