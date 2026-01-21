package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String mobile;
    private String profilePicture;
}