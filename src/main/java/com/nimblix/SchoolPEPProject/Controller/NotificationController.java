package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    //marks all notifications as read
    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok(
                Map.of("message", "All notifications marked as read")
        );
    }

    //Fetch Notification Flag Status API
    @GetMapping("/flag")
    public ResponseEntity<?> getNotificationFlag() {
        return ResponseEntity.ok(
                Map.of("hasNewNotification", notificationService.getNotificationFlag())
        );
    }
}