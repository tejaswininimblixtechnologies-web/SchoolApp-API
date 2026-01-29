package com.nimblix.SchoolPEPProject.Security;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String email = username.toLowerCase();

        Optional<User> userOptional = adminRepository.findByEmailId(email)
                .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                .map(u -> (User) u);

        if (userOptional.isEmpty()) {
            userOptional = teacherRepository.findByEmailId(email)
                    .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                    .map(u -> (User) u);
        }

        if (userOptional.isEmpty()) {
            userOptional = studentRepository.findByEmailId(email)
                    .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                    .map(u -> (User) u);
        }

        if (userOptional.isEmpty()) {
            userOptional = parentRepository.findByEmailId(email)
                    .filter(u -> SchoolConstants.ACTIVE.equalsIgnoreCase(u.getStatus()))
                    .map(u -> (User) u);
        }

        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("Active user not found for email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmailId(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()))
        );
    }
}
