package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.NonTeachingStaffRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.NonTeachingStaffAuthResponse;

public interface NonTeachingStaffService {

    String register(NonTeachingStaffRegisterRequest request);

    NonTeachingStaffAuthResponse login(String emailId, String password);

    String logout(String emailId);

    String sendForgotPasswordOtp(String emailId);

    String resetPasswordWithOtp(String emailId, String otp, String newPassword);
}
