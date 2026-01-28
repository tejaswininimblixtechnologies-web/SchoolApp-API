package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Admin;
import com.nimblix.SchoolPEPProject.Model.Notification;
import com.nimblix.SchoolPEPProject.Repository.AdminRepository;
import com.nimblix.SchoolPEPProject.Repository.NotificationRepository;
import com.nimblix.SchoolPEPProject.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final AdminRepository adminRepository;

    @Override
    public void createNotification(Long userId, String role, String title, String message) {

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setRole(role);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);

        notificationRepository.save(notification);

        // SET FLAG = TRUE
        adminRepository.findById(userId).ifPresent(admin -> {
            admin.setHasNewNotification(true);
            adminRepository.save(admin);
        });
    }

    @Override
    public void markAllAsRead() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Admin admin = adminRepository.findByEmailId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setHasNewNotification(false);
        adminRepository.save(admin);
    }

    @Override
    public Boolean getNotificationFlag() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        Admin admin = adminRepository.findByEmailId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return admin.getHasNewNotification();
    }
}