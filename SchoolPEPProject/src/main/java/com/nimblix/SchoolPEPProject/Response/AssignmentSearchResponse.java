package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssignmentSearchResponse {

    private Long assignmentId;
    private String subject;
    private String assignmentTitle;
    private String assignedDate;
    private String dueDate;
    private String status;
}
