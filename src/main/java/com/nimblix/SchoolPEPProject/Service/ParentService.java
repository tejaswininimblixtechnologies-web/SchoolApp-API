package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.ParentRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.AuthParentResponse;

public interface ParentService {

    AuthParentResponse signUp(ParentRegisterRequest request);
}
