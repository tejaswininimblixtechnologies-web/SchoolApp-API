package com.nimblix.SchoolPEPProject.Response;

import com.nimblix.SchoolPEPProject.Model.Designation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeacherProfileResponse {

    private Long id;
    private String teacherId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobile;
    private String prefix;
    private Designation designation;
    private String gender;
    private String status;
    private String address;
    private String profilePicture;
    private String staffType;
}
