package com.example.security.ws.facade;

import com.example.security.Authentification.user.User;
import com.example.security.Authentification.user.UserRepository;
import com.example.security.entity.Notification;
import com.example.security.service.facade.NotificationService;
import com.example.security.ws.converter.NotificationConverter;
import com.example.security.ws.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationConverter notificationConverter;
    private final UserRepository userRepository;

    @Autowired
    public NotificationController(
            NotificationService notificationService,
            NotificationConverter notificationConverter,
            UserRepository userRepository) {
        this.notificationService = notificationService;
        this.notificationConverter = notificationConverter;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> findById(@PathVariable Long id) {
        Notification notification = notificationService.findById(id);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notificationConverter.toDto(notification));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> findByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Notification> notifications = notificationService.findByUser(user);
        return ResponseEntity.ok(notificationConverter.toDtoList(notifications));
    }

    @GetMapping("/current-user")
    public ResponseEntity<List<NotificationDto>> findForCurrentUser() {
        // Récupérer l'utilisateur courant à partir du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Notification> notifications = notificationService.findByUser(currentUser);
        return ResponseEntity.ok(notificationConverter.toDtoList(notifications));
    }

    @GetMapping("/current-user/unread")
    public ResponseEntity<List<NotificationDto>> findUnreadForCurrentUser() {
        // Récupérer l'utilisateur courant à partir du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Notification> notifications = notificationService.findUnreadByUser(currentUser);
        return ResponseEntity.ok(notificationConverter.toDtoList(notifications));
    }

    @GetMapping("/current-user/count-unread")
    public ResponseEntity<Long> countUnreadForCurrentUser() {
        // Récupérer l'utilisateur courant à partir du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        Long count = notificationService.countUnreadByUser(currentUser.getId());
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notificationConverter.toDto(notification));
    }

    @PutMapping("/current-user/mark-all-read")
    public ResponseEntity<Void> markAllAsReadForCurrentUser() {
        // Récupérer l'utilisateur courant à partir du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        notificationService.markAllAsRead(currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Notification notification = notificationService.findById(id);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        // Vérifier que l'utilisateur courant est bien le destinataire
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);

        if (currentUser == null || !notification.getDestinataire().getId().equals(currentUser.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}