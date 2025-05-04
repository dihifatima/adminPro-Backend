package com.example.security.dao;

import com.example.security.Authentification.user.User;
import com.example.security.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Trouver toutes les notifications pour un destinataire
    List<Notification> findByDestinataire(User destinataire);

    // Trouver les notifications non vues d'un destinataire
    List<Notification> findByDestinataireAndVue(User destinataire, boolean vue);

    // Compter les notifications non vues d'un destinataire
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataire.id = :userId AND n.vue = false")
    Long countUnreadNotifications(@Param("userId") Long userId);

    // Marquer toutes les notifications d'un utilisateur comme lues
    @Query("UPDATE Notification n SET n.vue = true WHERE n.destinataire.id = :userId AND n.vue = false")
    void markAllAsRead(@Param("userId") Long userId);
}