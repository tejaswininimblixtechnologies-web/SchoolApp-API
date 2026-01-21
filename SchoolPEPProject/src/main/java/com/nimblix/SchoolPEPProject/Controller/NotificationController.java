package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Model.Admin;
import com.nimblix.SchoolPEPProject.Repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final AdminRepository adminRepository;

    // Reset notification flag
    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Admin admin = adminRepository
                .findByEmailId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setHasNewNotification(false);
        adminRepository.save(admin);

        return ResponseEntity.ok(
                Map.of("message", "Notifications marked as read")
        );
    }

    // Fetch notification flag
    @GetMapping("/flag")
    public ResponseEntity<?> getNotificationFlag() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Admin admin = adminRepository
                .findByEmailId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return ResponseEntity.ok(
                Map.of(
                        "hasNewNotification",
                        admin.getHasNewNotification()
                )
        );
    }
}