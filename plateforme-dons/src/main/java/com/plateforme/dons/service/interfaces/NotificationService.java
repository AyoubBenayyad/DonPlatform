package com.plateforme.dons.service.interfaces;

import com.plateforme.dons.entity.Notification;
import com.plateforme.dons.entity.User;

import java.util.List;

public interface NotificationService {
    Notification createNotification(User user, String message, String link);

    List<Notification> getUserNotifications(String username);

    long getUnreadCount(String username);

    void markAsRead(Long id, String username);
}
