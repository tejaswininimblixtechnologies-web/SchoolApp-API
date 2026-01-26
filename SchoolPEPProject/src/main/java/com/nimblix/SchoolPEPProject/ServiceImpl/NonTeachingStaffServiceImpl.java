package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Helper.MailHelper;
import com.nimblix.SchoolPEPProject.Model.Designation;
import com.nimblix.SchoolPEPProject.Model.NonTeachingStaff;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Repository.DesignationRepository;
import com.nimblix.SchoolPEPProject.Repository.NonTeachingStaffRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Request.NonTeachingStaffRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.NonTeachingStaffAuthResponse;
import com.nimblix.SchoolPEPProject.Security.JwtUtil;
import com.nimblix.SchoolPEPProject.Service.NonTeachingStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NonTeachingStaffServiceImpl implements NonTeachingStaffService {

    private final NonTeachingStaffRepository nonTeachingStaffRepository;
    private final RoleRepository roleRepository;
    private final DesignationRepository designationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final MailHelper mailHelper;

    @Override
    public String register(NonTeachingStaffRegisterRequest request) {

        if (nonTeachingStaffRepository.findByEmailId(request.getEmailId()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Designation designation = designationRepository.findById(request.getDesignationId())
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        NonTeachingStaff staff = new NonTeachingStaff();
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setEmailId(request.getEmailId());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setMobile(request.getMobile());
        staff.setSchoolId(request.getSchoolId());
        staff.setGender(request.getGender());
        staff.setAddress(request.getAddress());
        staff.setRole(role);
        staff.setDesignation(designation);
        staff.setStaffType(StaffType.NON_TEACHING);
        staff.setStatus(SchoolConstants.STATUS_SUCCESS);
        staff.setIsLogin(false);
        staff.setStaffCode("NTS-" + UUID.randomUUID().toString().substring(0, 8));

        nonTeachingStaffRepository.save(staff);

        return "Non Teaching Staff Registered Successfully";
    }

    @Override
    public NonTeachingStaffAuthResponse login(String emailId, String password) {

        NonTeachingStaff staff = nonTeachingStaffRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Non Teaching Staff not found"));

        if (!passwordEncoder.matches(password, staff.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                staff.getEmailId(),
                staff.getPassword(),
                new ArrayList<>()
        );

        String token = jwtUtil.generateToken(userDetails);

        staff.setIsLogin(true);
        nonTeachingStaffRepository.save(staff);

        return new NonTeachingStaffAuthResponse(
                SchoolConstants.STATUS_SUCCESS,
                token,
                staff.getEmailId(),
                staff.getStaffCode()
        );
    }

    @Override
    public String logout(String emailId) {

        NonTeachingStaff staff = nonTeachingStaffRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Non Teaching Staff not found"));

        staff.setIsLogin(false);
        nonTeachingStaffRepository.save(staff);

        return "Logout successful";
    }

    @Override
    public String sendForgotPasswordOtp(String emailId) {

        NonTeachingStaff staff = nonTeachingStaffRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Non Teaching Staff not found"));

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        staff.setResetOtp(otp);
        staff.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        nonTeachingStaffRepository.save(staff);

        mailHelper.sendOtpMail(
                staff.getEmailId(),
                staff.getFirstName(),
                otp,
                "Password Reset OTP"
        );

        return "OTP sent to registered email";
    }

    @Override
    public String resetPasswordWithOtp(String emailId, String otp, String newPassword) {

        NonTeachingStaff staff = nonTeachingStaffRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Non Teaching Staff not found"));

        if (!otp.equals(staff.getResetOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (staff.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        staff.setPassword(passwordEncoder.encode(newPassword));
        staff.setResetOtp(null);
        staff.setOtpExpiry(null);
        staff.setIsLogin(false);

        nonTeachingStaffRepository.save(staff);

        return "Password reset successful";
    }
}
