package com.nimblix.SchoolPEPProject.Service;

import java.util.List;
import com.nimblix.SchoolPEPProject.Model.Notification;

public interface NotificationService {

    void createNotification(Long userId, String role, String title, String message);

    void markAllAsRead();
    void markAllAsReadForParent();

    Boolean getNotificationFlag();

    List<Notification> getNotificationsForParent(Long parentId);

    void markNotificationAsRead(Long notificationId, Long parentId);
}