package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.NonTeachingStaffLoginRequest;
import com.nimblix.SchoolPEPProject.Request.NonTeachingStaffRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.NonTeachingStaffAuthResponse;
import com.nimblix.SchoolPEPProject.Service.NonTeachingStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/non-teaching-staff")
public class NonTeachingStaffController {

    @Autowired
    private NonTeachingStaffService service;

    @PostMapping("/register")
    public String register(@RequestBody NonTeachingStaffRegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public NonTeachingStaffAuthResponse login(@RequestBody NonTeachingStaffLoginRequest request) {
        return service.login(request.getEmailId(), request.getPassword());
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String emailId) {
        return service.logout(emailId);
    }

    @PostMapping("/forgot-password/otp")
    public String sendOtp(@RequestParam String emailId) {
        return service.sendForgotPasswordOtp(emailId);
    }

    @PostMapping("/forgot-password/reset")
    public String resetPassword(
            @RequestParam String emailId,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        return service.resetPasswordWithOtp(emailId, otp, newPassword);
    }
}
