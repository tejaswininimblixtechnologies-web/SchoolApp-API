package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentContextResponse {

    private Long studentId;
    private String firstName;
    private String lastName;
    private String email;
    private Long classId;
    private String section;
    private String academicYear;
    private String rollNumber;
    private Long schoolId;
}
