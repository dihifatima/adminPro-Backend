package com.example.security.service.impl;

import com.example.security.Authentification.user.User;
import com.example.security.dao.AdminRepository;
import com.example.security.dao.NotificationRepository;
import com.example.security.entity.Admin;
import com.example.security.entity.DemandeService;
import com.example.security.entity.Notification;
import com.example.security.service.facade.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            AdminRepository adminRepository) {
        this.notificationRepository = notificationRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Notification save(Notification notification) {
        // S'assurer que la date de création est définie
        if (notification.getDateCreation() == null) {
            notification.setDateCreation(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }

    @Override
    public Notification createNotificationForUser(User destinataire, String sujet, String message) {
        Notification notification = new Notification();
        notification.setDestinataire(destinataire);
        notification.setSujet(sujet);
        notification.setMessage(message);
        notification.setDateCreation(LocalDateTime.now());
        notification.setVue(false);

        return save(notification);
    }

    @Override
    public Notification createNotificationForDemandeService(User destinataire, DemandeService demandeService, String sujet, String message) {
        Notification notification = new Notification();
        notification.setDestinataire(destinataire);
        notification.setDemandeService(demandeService);
        notification.setSujet(sujet);
        notification.setMessage(message);
        notification.setDateCreation(LocalDateTime.now());
        notification.setVue(false);

        return save(notification);
    }

    @Override
    public void notifyAdminsByDepartement(String departement, String sujet, String message, DemandeService demandeService) {
        // Trouver tous les admins du département spécifié
        List<Admin> admins = adminRepository.findAll().stream()
                .filter(admin -> admin.getAdminDepartement().equals(departement))
                .collect(Collectors.toList());

        // Créer une notification pour chaque admin
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setDestinataire(admin);
            notification.setDemandeService(demandeService);
            notification.setSujet(sujet);
            notification.setMessage(message);
            notification.setDateCreation(LocalDateTime.now());
            notification.setVue(false);

            save(notification);
        }
    }

    @Override
    @Transactional
    public Notification markAsRead(Long idNotification) {
        Optional<Notification> optionalNotification = notificationRepository.findById(idNotification);
        if (optionalNotification.isEmpty()) {
            return null;
        }

        Notification notification = optionalNotification.get();
        notification.setVue(true);
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    @Override
    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Notification> findByUser(User user) {
        return notificationRepository.findByDestinataire(user);
    }

    @Override
    public List<Notification> findUnreadByUser(User user) {
        return notificationRepository.findByDestinataireAndVue(user, false);
    }

    @Override
    public Long countUnreadByUser(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}