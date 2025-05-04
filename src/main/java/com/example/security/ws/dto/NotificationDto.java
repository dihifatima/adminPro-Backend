package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long idNotification;
    private Long destinataireId;
    private String destinataireNom;
    private String sujet;
    private String message;
    private LocalDateTime dateCreation;
    private boolean vue;
    private Long demandeServiceId;  // ID de la demande de service associée (si applicable)
}
