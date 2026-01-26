package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.AdminRepository;
import com.nimblix.SchoolPEPProject.Repository.DesignationRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
//import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import com.nimblix.SchoolPEPProject.Request.AdminProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final DesignationRepository designationRepository;

    @Override
    public String submitEmail(String email) {

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (adminRepository.existsByEmailId(email)) {
            return "Email already registered.";
        }

        return "Email accepted. Continue to account creation.";
    }

    @Override
    public Long createAdminAccount(AdminAccountCreateRequest request) {

        // Email validation
        if (request.getEmail() == null ||
                !request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid Email Format");
        }

        if (adminRepository.existsByEmailId(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Mobile validation
        if (request.getAdminMobileNo() == null || request.getAdminMobileNo().length() != 10) {
            throw new IllegalArgumentException("Invalid mobile number (must be 10 digits)");
        }

        if (adminRepository.existsByMobile(request.getAdminMobileNo())) {
            throw new IllegalArgumentException("Mobile number already registered");
        }

        // Password validation
        if (!request.getPassword().equals(request.getReEnterPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Role role = roleRepository.findByRoleName(SchoolConstants.ADMIN_ROLE);

        Designation adminDesignation =
                designationRepository
                        .findByDesignationName(SchoolConstants.ADMIN)
                        .orElseThrow(() ->
                                new RuntimeException("Admin designation not found"));

        Admin admin = new Admin();
        admin.setFirstName(request.getAdminFirstName());
        admin.setLastName(request.getAdminLastName());
        admin.setEmailId(request.getEmail());
        admin.setMobile(request.getAdminMobileNo());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        admin.setRole(role);
        admin.setStaffType(StaffType.NON_TEACHING);
        admin.setDesignation(adminDesignation);
        admin.setStatus(SchoolConstants.STATUS);
        admin.setIsLogin(false);

        Admin savedAdmin = adminRepository.save(admin);

        return savedAdmin.getId();
    }

    @Override
    public List<Student> getStudentList(
            Long schoolId,
            Long classId,
            String section,
            String status
    ) {
        return studentRepository.findByAllFilters(
                schoolId,
                classId,
                section,
                status
        );
    }

    // Fetches profile details of the currently logged-in admin only
    @Override
    public AdminProfileResponse getLoggedInAdminProfile() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        Admin admin = adminRepository.findByEmailId(email)
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        AdminProfileResponse response = new AdminProfileResponse();
        response.setAdminId(admin.getId());
        response.setUserId(admin.getId());
        response.setFirstName(admin.getFirstName());
        response.setLastName(admin.getLastName());
        response.setEmailId(admin.getEmailId());
        response.setMobile(admin.getMobile());
        response.setGender(admin.getGender());
        response.setDesignation(admin.getDesignation());
        response.setProfilePicture(admin.getProfilePicture());
        response.setSchoolId(admin.getSchoolId());

        return response;
    }

    /**
     * Updates profile details of the currently logged-in admin.
     * Supports partial updates.
     * Email field is intentionally not updatable.
     * Mobile number must be 10 digits.
     */
    @Override
    public void updateLoggedInAdminProfile(AdminProfileUpdateRequest request) {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Admin admin = adminRepository.findByEmailId(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        if (request.getFirstName() != null)
            admin.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            admin.setLastName(request.getLastName());

        if (request.getMobile() != null) {
            if (!request.getMobile().matches("\\d{10}"))
                throw new IllegalArgumentException("Invalid mobile number");
            admin.setMobile(request.getMobile());
        }

        if (request.getProfilePicture() != null)
            admin.setProfilePicture(request.getProfilePicture());

        adminRepository.save(admin);
    }

    /**
     * Soft Delete Admin Profile
     *
     * Deactivates the logged-in admin account.
     * - Sets status to DELETED
     * - Invalidates active login session
     * - Hard delete is NOT performed
     *
     * This ensures audit safety and prevents future login.
     */
    @Override
    public void softDeleteLoggedInAdmin() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Admin admin = adminRepository.findByEmailId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setStatus(SchoolConstants.DELETED);
        admin.setIsLogin(false);

        adminRepository.save(admin);
    }
}

