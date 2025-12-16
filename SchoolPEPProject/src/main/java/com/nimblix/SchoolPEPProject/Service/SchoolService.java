package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Request.SchoolRegistrationRequest;

public interface SchoolService {

    School registerSchool(SchoolRegistrationRequest request);
}
