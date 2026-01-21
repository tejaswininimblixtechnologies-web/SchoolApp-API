package com.nimblix.SchoolPEPProject.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherResponse {

    private Long id;

    private String name;        // firstName + lastName

    private String subject;     // subjectName

    private String contact;     // mobile / emailId

    private String designation;
}
