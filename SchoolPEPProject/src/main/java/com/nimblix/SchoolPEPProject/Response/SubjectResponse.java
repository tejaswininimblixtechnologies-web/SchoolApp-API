package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubjectResponse {

    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String description;
    private String status;
}
