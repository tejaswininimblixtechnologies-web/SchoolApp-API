package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StudentProfileResponse {

    private Long studentId;
    private String profilePhoto;
    private String firstName;
    private String lastName;
    private String fullName;
    private String rollNumber;
    private Long classId;
    private String section;
    private String academicYear;
    private List<String> subjectsEnrolled;
    private String emailId;
    private String mobile;
    private String address;
    private String parentName;
    private String parentContact;
    private String parentEmail;
    private String status;
    private String schoolName;
}
