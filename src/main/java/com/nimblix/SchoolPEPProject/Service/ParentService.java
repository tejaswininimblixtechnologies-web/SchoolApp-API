package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.ParentRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.AuthParentResponse;
import com.nimblix.SchoolPEPProject.Model.Parent;
import java.util.Map;

public interface ParentService {

    AuthParentResponse signUp(ParentRegisterRequest request);
    Parent getParentProfile(String email);
    Parent updateParentProfile(String email, Map<String, Object> request);
    Parent changeParentPassword(String email, String oldPassword, String newPassword);
}
