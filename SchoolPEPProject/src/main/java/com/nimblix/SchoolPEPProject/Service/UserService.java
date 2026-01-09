package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.UserRegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> registerUser(UserRegistrationRequest request);
}
