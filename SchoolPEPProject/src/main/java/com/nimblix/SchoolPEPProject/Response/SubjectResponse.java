package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubjectResponse {

    private Long id;
    private String subjectName;
    private String code;
    private String subDescription;
    private Long classRoomId;
    private Long totalMarks;
    private Long marksObtained;
}
