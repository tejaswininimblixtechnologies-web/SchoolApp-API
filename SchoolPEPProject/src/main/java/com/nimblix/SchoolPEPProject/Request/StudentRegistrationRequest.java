package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class StudentRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String reEnterPassword;
    private Long schoolId;
    private String mobile;
    private Long classId;
    private String section;


}
