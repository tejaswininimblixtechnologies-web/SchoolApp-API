package com.nimblix.SchoolPEPProject.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request DTO for updating Admin profile.
 * Only provided fields will be updated.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProfileUpdateRequest {

    private String firstName;
    private String lastName;
    private String mobile;
    private String profilePicture;
}
