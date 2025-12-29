package com.plateforme.dons.repository;

import com.plateforme.dons.entity.Notification;
import com.plateforme.dons.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByDateCreationDesc(User user);

    long countByUserAndIsReadFalse(User user);
}
