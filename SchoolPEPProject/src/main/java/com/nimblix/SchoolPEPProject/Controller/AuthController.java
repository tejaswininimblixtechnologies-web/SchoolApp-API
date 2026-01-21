package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.*;
import com.nimblix.SchoolPEPProject.Enum.Status;
import com.nimblix.SchoolPEPProject.Request.AuthLoginRequest;
import com.nimblix.SchoolPEPProject.Response.AuthLoginResponse;
import com.nimblix.SchoolPEPProject.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequest request) {

        String email = request.getEmail().toLowerCase();
        String role = request.getRole().toUpperCase();

        Long userId;
        String firstName;
        String lastName;
        String dbRole;

        switch (role) {

            case SchoolConstants.ADMIN -> {
                Admin admin = adminRepository.findByEmailId(email)
                        .orElseThrow(() -> new RuntimeException("Admin not found"));

                if (!SchoolConstants.ACTIVE.equalsIgnoreCase(admin.getStatus()))
                    throw new RuntimeException("Admin inactive");

                userId = admin.getId();
                firstName = admin.getFirstName();
                lastName = admin.getLastName();
                dbRole = admin.getRole().getRoleName();
            }

            case SchoolConstants.TEACHER -> {
                Teacher teacher = teacherRepository.findByEmailId(email)
                        .orElseThrow(() -> new RuntimeException("Teacher not found"));

                if (teacher.getStatus() != Status.ACTIVE)
                    throw new RuntimeException("Teacher inactive");

                userId = teacher.getId();
                firstName = teacher.getFirstName();
                lastName = teacher.getLastName();
                dbRole = SchoolConstants.TEACHER;
            }

            case SchoolConstants.STUDENT -> {
                Student student = studentRepository.findByEmailId(email)
                        .orElseThrow(() -> new RuntimeException("Student not found"));

                if (!SchoolConstants.ACTIVE.equalsIgnoreCase(student.getStatus()))
                    throw new RuntimeException("Student inactive");

                userId = student.getId();
                firstName = student.getFirstName();
                lastName = student.getLastName();
                dbRole = student.getRole().getRoleName();
            }

            case SchoolConstants.PARENT -> {
                Parent parent = parentRepository.findByEmailId(email)
                        .orElseThrow(() -> new RuntimeException("Parent not found"));

                if (!SchoolConstants.ACTIVE.equalsIgnoreCase(parent.getStatus()))
                    throw new RuntimeException("Parent inactive");

                userId = parent.getId();
                firstName = parent.getFirstName();
                lastName = parent.getLastName();
                dbRole = parent.getRole().getRoleName();
            }

            default -> throw new RuntimeException("Invalid role");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(
                userDetailsService.loadUserByUsername(email)
        );

        AuthLoginResponse response = new AuthLoginResponse();
        response.setUserId(userId);
        response.setFirstName(firstName);
        response.setLastName(lastName);
        response.setEmail(email);
        response.setRole(dbRole);
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}

