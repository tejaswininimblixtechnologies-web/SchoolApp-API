package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssignmentSubmissionResponse {

    private Long submissionId;
    private Long assignmentId;
    private String submissionStatus;
    private String submissionDate;
    private String evaluationStatus;
    private Integer marksObtained;
    private String teacherFeedback;
    private String fileName;
    private String remarks;
}
