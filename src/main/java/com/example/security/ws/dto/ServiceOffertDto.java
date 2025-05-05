package com.example.security.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data  // Annotation Lombok pour générer automatiquement getters, setters, equals, hashCode et toString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOffertDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
