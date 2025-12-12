package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Admin;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.AdminRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
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

        if (request.getEmail() == null ||
                !request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid Email Format");
        }

        if (adminRepository.existsByEmailId(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (request.getAdminMobileNo() == null || request.getAdminMobileNo().length() != 10) {
            throw new IllegalArgumentException("Invalid mobile number (must be 10 digits)");
        }

        if (adminRepository.existsByMobileNumber(request.getAdminMobileNo())) {
            throw new IllegalArgumentException("Mobile number already registered");
        }

        if (request.getAdminName() == null || request.getAdminName().isEmpty()) {
            throw new IllegalArgumentException("Admin name is required");
        }

        if (!request.getPassword().equals(request.getReEnterPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User();
        user.setFullName(request.getAdminName());
        user.setEmailId(request.getEmail());
        user.setMobile(request.getAdminMobileNo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDesignation(SchoolConstants.ADMIN_ROLE);
        user.setStatus(SchoolConstants.STATUS);
        user.setIsLogin(false);

        Role role = roleRepository.findByRoleName(SchoolConstants.ADMIN_ROLE);
        user.setRole(role);

        userRepository.save(user);

        Admin admin = new Admin();
        admin.setFullName(request.getAdminName());
        admin.setMobileNumber(request.getAdminMobileNo());
        admin.setEmailId(request.getEmail());
        admin.setPassword(user.getPassword());

        Admin savedAdmin = adminRepository.save(admin);

        return savedAdmin.getId();
    }

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

}
