package com.nimblix.SchoolPEPProject.Request;

import lombok.Data;

@Data
public class NonTeachingStaffLoginRequest {
    private String emailId;
    private String password;
}
