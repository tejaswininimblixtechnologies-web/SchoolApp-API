package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentMarksRequest {

    private Long studentId;
    private String subject;
    private Double marks;
    private String grade;
    private String examType;
}