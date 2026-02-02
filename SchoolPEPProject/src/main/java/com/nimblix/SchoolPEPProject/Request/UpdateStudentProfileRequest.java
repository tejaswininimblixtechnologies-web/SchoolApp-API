package com.nimblix.SchoolPEPProject.Request;

import lombok.Data;

@Data
public class UpdateStudentProfileRequest {

    private String profilePhoto;
    private String mobile;
    private String emailId;
    private String address;

    // Validation: Only these fields are editable
    // Non-editable fields: firstName, lastName, rollNumber, classId, section, etc.
}
