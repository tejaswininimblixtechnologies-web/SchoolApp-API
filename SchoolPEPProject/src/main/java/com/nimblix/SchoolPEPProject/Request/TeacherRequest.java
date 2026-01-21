package com.nimblix.SchoolPEPProject.Request;

import com.nimblix.SchoolPEPProject.Enum.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherRequest {

    /*  Basic Details  */

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String emailId;

    @NotBlank(message = "Mobile number is required")
    private String mobile;

    /* Academic Details */

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    private String subjectName;

    private String designation;

    /*  School Mapping  */

    @NotNull(message = "School ID is required")
    private Long schoolId;

    private String gender;

    /*  System (used in UPDATE only) */

    private Status status;
}
