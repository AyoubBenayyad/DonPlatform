package com.plateforme.dons.service.impl;

import com.plateforme.dons.entity.Notification;
import com.plateforme.dons.entity.User;
import com.plateforme.dons.exception.ResourceNotFoundException;
import com.plateforme.dons.repository.NotificationRepository;
import com.plateforme.dons.repository.UserRepository;
import com.plateforme.dons.service.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Notification createNotification(User user, String message, String link) {
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .link(link)
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        return notificationRepository.findByUserOrderByDateCreationDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Override
    @Transactional
    public void markAsRead(Long id, String username) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));

        if (!notification.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
