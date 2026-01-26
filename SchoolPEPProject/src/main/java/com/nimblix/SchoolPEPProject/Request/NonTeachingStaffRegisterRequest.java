package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonTeachingStaffRegisterRequest {
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String mobile;
    private Long schoolId;
    private String gender;
    private String address;
    private Long roleId;
    private Long designationId;
}
