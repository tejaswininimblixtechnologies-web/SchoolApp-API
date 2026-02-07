package com.nimblix.SchoolPEPProject.ServiceImpl;

import java.util.Map;
import com.nimblix.SchoolPEPProject.Model.Parent;
import com.nimblix.SchoolPEPProject.Repository.ParentRepository;
import com.nimblix.SchoolPEPProject.Request.ParentRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.AuthParentResponse;
import com.nimblix.SchoolPEPProject.Service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

     private final ParentRepository parentRepository;
     private final PasswordEncoder passwordEncoder;

    @Override
    public AuthParentResponse signUp(ParentRegisterRequest request) {


        if (parentRepository.existsByEmailId(request.getEmailId())) {
            return new AuthParentResponse(false, "Email already registered", null);
        }
        Parent parent = new Parent();
        parent.setFirstName(request.getFullName());
        parent.setEmailId(request.getEmailId());
        parent.setMobile(request.getContactNumber());
        parent.setAddress(request.getAddress());
        parent.setSchoolId(request.getSchoolId());
        parent.setRole(request.getRole());

        parent.setStatus("ACTIVE");
        parent.setIsLogin(false);

        // Encrypt password
        parent.setPassword(passwordEncoder.encode(request.getPassword()));

        parentRepository.save(parent);


        return new AuthParentResponse(true, "Parent registered successfully", null);
    }

    @Override
    public Parent getParentProfile(String email) {
        return parentRepository.findByEmailId(email).orElse(null);
    }

    @Override
    public Parent updateParentProfile(String email, Map<String, Object> request) {

        Parent parent = parentRepository.findByEmailId(email).orElse(null);

        if (parent == null) {
            return null;
        }

        if (request.containsKey("fullName")) {
            parent.setFirstName((String) request.get("fullName"));
        }


        if (request.containsKey("mobile")) {
            parent.setMobile((String) request.get("mobile"));
        }

        if (request.containsKey("address")) {
            parent.setAddress((String) request.get("address"));
        }

        if (request.containsKey("profilePicture")) {
            parent.setProfilePicture((String) request.get("profilePicture"));
        }

        return parentRepository.save(parent);
    }

    @Override
    public Parent changeParentPassword(String email, String oldPassword, String newPassword) {

        Parent parent = parentRepository.findByEmailId(email).orElse(null);

        if (parent == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (!passwordEncoder.matches(oldPassword, parent.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        parent.setPassword(passwordEncoder.encode(newPassword));
        return parentRepository.save(parent);
    }
}
