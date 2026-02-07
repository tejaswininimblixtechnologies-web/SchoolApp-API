package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Request.SchoolRegistrationRequest;
import com.nimblix.SchoolPEPProject.Service.SchoolService;
import com.nimblix.SchoolPEPProject.Service.UserService;
import com.nimblix.SchoolPEPProject.Response.StudentDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/school/user")
@RequiredArgsConstructor
public class UserController {

    private final SchoolService schoolService;
    private final UserService userService;
    private final StudentService studentService;
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerSchool(
            @RequestBody SchoolRegistrationRequest request) {

        School school = schoolService.registerSchool(request);

        Map<String, Object> response = new HashMap<>();
        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "School registered successfully");
        response.put("data", school.getSchoolId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/api/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {

        List<StudentDetailsResponse> students = studentService.getAllStudents();

        Map<String, Object> response = new HashMap<>();
        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "Users fetched successfully");
        response.put("data", students);

        return ResponseEntity.ok(response);
    }
}
