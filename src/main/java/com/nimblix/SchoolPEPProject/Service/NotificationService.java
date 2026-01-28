package com.nimblix.SchoolPEPProject.Service;

public interface NotificationService {

    void createNotification(Long userId, String role, String title, String message);

    void markAllAsRead();

    Boolean getNotificationFlag();
}