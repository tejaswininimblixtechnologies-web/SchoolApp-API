package com.nimblix.SchoolPEPProject.Request;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String studentId;
    private String parentId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String reEnterPassword;
    private String mobileNumber;
    private String role;
    private Long schoolId;
}
