package com.example.security.service.facade;

import com.example.security.Authentification.user.User;
import com.example.security.entity.DemandeService;
import com.example.security.entity.Notification;

import java.util.List;

public interface NotificationService {
    // Créer une notification
    Notification save(Notification notification);

    // Créer une notification pour un utilisateur spécifique
    Notification createNotificationForUser(User destinataire, String sujet, String message);

    // Créer une notification pour une demande de service
    Notification createNotificationForDemandeService(User destinataire, DemandeService demandeService, String sujet, String message);

    // Créer une notification pour tous les admins d'un département
    void notifyAdminsByDepartement(String departement, String sujet, String message, DemandeService demandeService);

    // Marquer une notification comme lue
    Notification markAsRead(Long idNotification);

    // Marquer toutes les notifications d'un utilisateur comme lues
    void markAllAsRead(Long userId);

    // Rechercher une notification par ID
    Notification findById(Long id);

    // Récupérer toutes les notifications d'un utilisateur
    List<Notification> findByUser(User user);

    // Récupérer les notifications non lues d'un utilisateur
    List<Notification> findUnreadByUser(User user);

    // Compter les notifications non lues d'un utilisateur
    Long countUnreadByUser(Long userId);

    // Supprimer une notification
    void delete(Long id);
}